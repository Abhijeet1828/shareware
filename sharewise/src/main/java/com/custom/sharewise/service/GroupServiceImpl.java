package com.custom.sharewise.service;

import java.util.Date;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.exception.ResourceNotFoundException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.dto.UserGroupDto;
import com.custom.sharewise.model.Group;
import com.custom.sharewise.repository.GroupRepository;
import com.custom.sharewise.request.CreateOrUpdateGroupRequest;
import com.custom.sharewise.validation.BusinessValidationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED, transactionManager = "transactionManager")
public class GroupServiceImpl implements GroupService {

	private final GroupRepository groupRepository;
	private final UserGroupMappingService userGroupMappingService;
	private final UserRolesService userRolesService;
	private final BusinessValidationService businessValidationService;
	private final ModelMapper modelMapper;

	@CacheEvict(value = "users", key = "#userDetails.username")
	@Override
	public Group createGroup(CreateOrUpdateGroupRequest createGroupRequest, CustomUserDetails userDetails) {
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		modelMapper.typeMap(CreateOrUpdateGroupRequest.class, Group.class)
				.addMappings(mapper -> mapper.skip(Group::setGroupId));
		Group group = modelMapper.map(createGroupRequest, Group.class);
		group.setCreatedBy(userDetails.getUserId());
		group.setCreatedTimestamp(new Date());
		group.setModifiedTimestamp(new Date());
		group.setIsActive(true);

		group = groupRepository.save(group);

		userGroupMappingService.addUserToGroup(group.getGroupId(), userDetails.getUserId());

		userRolesService.addRoleForUser(userDetails.getUserId(), Constants.ROLE_ADMIN);

		return group;
	}

	@Override
	public Group updateGroup(CreateOrUpdateGroupRequest updateGroupRequest, CustomUserDetails userDetails) {
		Group existingGroup = groupRepository.findById(updateGroupRequest.getGroupId())
				.orElseThrow(() -> new ResourceNotFoundException(FailureConstants.GROUP_NOT_FOUND.getFailureCode(),
						FailureConstants.GROUP_NOT_FOUND.getFailureMsg()));

		businessValidationService.validate(Map.of(Constants.VALIDATION_GROUP_ADMIN,
				new UserGroupDto(existingGroup.getGroupId(), userDetails.getUserId(), null)));

		modelMapper.getConfiguration().setSkipNullEnabled(true);
		modelMapper.typeMap(CreateOrUpdateGroupRequest.class, Group.class)
				.addMappings(mapper -> mapper.skip(Group::setGroupId));
		modelMapper.map(updateGroupRequest, existingGroup);

		existingGroup.setModifiedTimestamp(new Date());

		return groupRepository.save(existingGroup);
	}

	@Override
	public void deleteGroup(Long groupId, CustomUserDetails userDetails) {
		Group existingGroup = groupRepository.findById(groupId)
				.orElseThrow(() -> new ResourceNotFoundException(FailureConstants.GROUP_NOT_FOUND.getFailureCode(),
						FailureConstants.GROUP_NOT_FOUND.getFailureMsg()));

		businessValidationService.validate(Map.of(Constants.VALIDATION_GROUP_ADMIN,
				new UserGroupDto(existingGroup.getGroupId(), userDetails.getUserId(), null)));

		existingGroup.setIsActive(false);
		existingGroup.setModifiedTimestamp(new Date());

		groupRepository.save(existingGroup);
	}

}
