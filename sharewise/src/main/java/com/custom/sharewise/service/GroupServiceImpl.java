package com.custom.sharewise.service;

import java.util.Date;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.dto.UserGroupDto;
import com.custom.sharewise.model.Group;
import com.custom.sharewise.repository.GroupRepository;
import com.custom.sharewise.request.CreateOrUpdateGroupRequest;
import com.custom.sharewise.validation.BusinessValidationService;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { CommonException.class,
		UnauthorizedException.class }, transactionManager = "transactionManager")
public class GroupServiceImpl implements GroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);

	private final GroupRepository groupRepository;
	private final UserGroupMappingService userGroupMappingService;
	private final UserRolesService userRolesService;
	private final BusinessValidationService businessValidationService;
	private final ModelMapper modelMapper;

	public GroupServiceImpl(GroupRepository groupRepository, UserGroupMappingService userGroupMappingService,
			UserRolesService userRolesService, BusinessValidationService businessValidationService,
			ModelMapper modelMapper) {
		this.groupRepository = groupRepository;
		this.userGroupMappingService = userGroupMappingService;
		this.userRolesService = userRolesService;
		this.businessValidationService = businessValidationService;
		this.modelMapper = modelMapper;
	}

	@CacheEvict(value = "users", key = "#userDetails.username")
	@Override
	public Object createGroup(CreateOrUpdateGroupRequest createGroupRequest, CustomUserDetails userDetails)
			throws CommonException {
		try {
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
		} catch (Exception e) {
			LOGGER.error("Exception in createGroup", e);
			if (e instanceof CommonException ce)
				throw ce;
			else
				throw new CommonException(FailureConstants.CREATE_GROUP_ERROR.getFailureCode(),
						FailureConstants.CREATE_GROUP_ERROR.getFailureMsg());
		}
	}

	@Override
	public Object updateGroup(CreateOrUpdateGroupRequest updateGroupRequest, CustomUserDetails userDetails)
			throws CommonException, UnauthorizedException {
		try {
			Group existingGroup = groupRepository.findById(updateGroupRequest.getGroupId())
					.orElseThrow(() -> new CommonException(FailureConstants.GROUP_NOT_FOUND.getFailureCode(),
							FailureConstants.GROUP_NOT_FOUND.getFailureMsg()));

			businessValidationService.validate(Map.of(Constants.VALIDATION_GROUP_ADMIN,
					new UserGroupDto(existingGroup.getGroupId(), userDetails.getUserId(), null)));

			modelMapper.getConfiguration().setSkipNullEnabled(true);
			modelMapper.typeMap(CreateOrUpdateGroupRequest.class, Group.class)
					.addMappings(mapper -> mapper.skip(Group::setGroupId));
			modelMapper.map(updateGroupRequest, existingGroup);

			existingGroup.setModifiedTimestamp(new Date());

			return groupRepository.save(existingGroup);
		} catch (Exception e) {
			LOGGER.error("Exception in updateGroup", e);
			switch (e) {
			case CommonException ce -> throw ce;
			case UnauthorizedException au -> throw au;
			default -> throw new CommonException(FailureConstants.UPDATE_GROUP_ERROR.getFailureCode(),
					FailureConstants.UPDATE_GROUP_ERROR.getFailureMsg());
			}
		}
	}

	@Override
	public void deleteGroup(Long groupId, CustomUserDetails userDetails) throws CommonException, UnauthorizedException {
		try {
			Group existingGroup = groupRepository.findById(groupId)
					.orElseThrow(() -> new CommonException(FailureConstants.GROUP_NOT_FOUND.getFailureCode(),
							FailureConstants.GROUP_NOT_FOUND.getFailureMsg()));

			businessValidationService.validate(Map.of(Constants.VALIDATION_GROUP_ADMIN,
					new UserGroupDto(existingGroup.getGroupId(), userDetails.getUserId(), null)));

			existingGroup.setIsActive(false);
			existingGroup.setModifiedTimestamp(new Date());

			groupRepository.save(existingGroup);
		} catch (Exception e) {
			LOGGER.error("Exception in deleteGroup", e);
			switch (e) {
			case CommonException ce -> throw ce;
			case UnauthorizedException au -> throw au;
			default -> throw new CommonException(FailureConstants.DELETE_GROUP_ERROR.getFailureCode(),
					FailureConstants.DELETE_GROUP_ERROR.getFailureMsg());
			}
		}
	}

}
