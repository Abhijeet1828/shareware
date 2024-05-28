package com.custom.sharewise.service;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.model.Group;
import com.custom.sharewise.repository.GroupRepository;
import com.custom.sharewise.request.CreateOrUpdateGroupRequest;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = CommonException.class, transactionManager = "transactionManager")
public class GroupServiceImpl implements GroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);

	private final GroupRepository groupRepository;
	private final UserGroupMappingService userGroupMappingService;
	private final UserRolesService userRolesService;
	private final ModelMapper modelMapper;

	public GroupServiceImpl(GroupRepository groupRepository, UserGroupMappingService userGroupMappingService,
			UserRolesService userRolesService, ModelMapper modelMapper) {
		this.groupRepository = groupRepository;
		this.userGroupMappingService = userGroupMappingService;
		this.userRolesService = userRolesService;
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
			throw new CommonException(FailureConstants.CREATE_GROUP_ERROR.getFailureCode(),
					FailureConstants.CREATE_GROUP_ERROR.getFailureMsg());
		}
	}

	@Override
	public Object updateGroup(CreateOrUpdateGroupRequest updateGroupRequest, CustomUserDetails userDetails)
			throws CommonException {
		try {
			Group existingGroup = groupRepository.findById(updateGroupRequest.getGroupId()).orElseThrow();
			if (!existingGroup.getCreatedBy().equals(userDetails.getUserId())) {
				LOGGER.error("Unauthorized! UserId {} is not the admin of the GroupId {}", userDetails.getUserId(),
						existingGroup.getGroupId());
				return 1;
			}

			modelMapper.getConfiguration().setSkipNullEnabled(true);
			modelMapper.typeMap(CreateOrUpdateGroupRequest.class, Group.class)
					.addMappings(mapper -> mapper.skip(Group::setGroupId));
			modelMapper.map(updateGroupRequest, existingGroup);

			existingGroup.setModifiedTimestamp(new Date());

			return groupRepository.save(existingGroup);
		} catch (Exception e) {
			LOGGER.error("Exception in updateGroup", e);
			throw new CommonException(FailureConstants.UPDATE_GROUP_ERROR.getFailureCode(),
					FailureConstants.UPDATE_GROUP_ERROR.getFailureMsg());
		}
	}

	@Override
	public int deleteGroup(Long groupId, CustomUserDetails userDetails) throws CommonException {
		try {
			Group existingGroup = groupRepository.findById(groupId).orElseThrow();
			if (!existingGroup.getCreatedBy().equals(userDetails.getUserId())) {
				LOGGER.error("Unauthorized! UserId {} is not the admin of the GroupId {}", userDetails.getUserId(),
						existingGroup.getGroupId());
				return 1;
			}

			existingGroup.setIsActive(false);
			existingGroup.setModifiedTimestamp(new Date());

			groupRepository.save(existingGroup);
			return 2;
		} catch (Exception e) {
			LOGGER.error("Exception in deleteGroup", e);
			throw new CommonException(FailureConstants.DELETE_GROUP_ERROR.getFailureCode(),
					FailureConstants.DELETE_GROUP_ERROR.getFailureMsg());
		}
	}

}
