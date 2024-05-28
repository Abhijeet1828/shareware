package com.custom.sharewise.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.model.Group;
import com.custom.sharewise.model.UserGroupMapping;
import com.custom.sharewise.repository.GroupRepository;
import com.custom.sharewise.repository.UserGroupMappingRepository;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = CommonException.class, transactionManager = "transactionManager")
public class UserGroupMappingServiceImpl implements UserGroupMappingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupMappingServiceImpl.class);

	private final UserGroupMappingRepository userGroupMappingRepository;
	private final GroupRepository groupRepository;

	public UserGroupMappingServiceImpl(UserGroupMappingRepository userGroupMappingRepository,
			GroupRepository groupRepository) {
		this.userGroupMappingRepository = userGroupMappingRepository;
		this.groupRepository = groupRepository;
	}

	@Override
	public void addUserToGroup(Long groupId, Long userId) throws CommonException {
		try {
			createUserGroupMapping(groupId, userId);
		} catch (Exception e) {
			LOGGER.error("Exception in addUserToGroup", e);
			throw new CommonException(FailureConstants.ADD_USER_TO_GROUP_ERROR.getFailureCode(),
					FailureConstants.ADD_USER_TO_GROUP_ERROR.getFailureMsg());
		}
	}

	@Override
	public void addUserToGroup(Long groupId, Long userId, CustomUserDetails userDetails) throws CommonException {
		try {
			Group existingGroup = groupRepository.findFirstByGroupIdAndIsActiveTrue(groupId)
					.orElseThrow(() -> new CommonException(FailureConstants.GROUP_NOT_FOUND.getFailureCode(),
							FailureConstants.GROUP_NOT_FOUND.getFailureMsg()));

			createUserGroupMapping(existingGroup.getGroupId(), userId);
		} catch (CommonException ce) {
			throw ce;
		} catch (Exception e) {
			LOGGER.error("Exception in addUserToGroup", e);
			throw new CommonException(FailureConstants.ADD_USER_TO_GROUP_ERROR.getFailureCode(),
					FailureConstants.ADD_USER_TO_GROUP_ERROR.getFailureMsg());
		}
	}

	@Override
	public void removeUserFromGroup(Long groupId, Long userId) throws CommonException {
		try {
			Group existingGroup = groupRepository.findFirstByGroupIdAndIsActiveTrue(groupId)
					.orElseThrow(() -> new CommonException(FailureConstants.GROUP_NOT_FOUND.getFailureCode(),
							FailureConstants.GROUP_NOT_FOUND.getFailureMsg()));

			UserGroupMapping userGroupMapping = userGroupMappingRepository
					.findFirstByGroupIdAndUserIdAndIsRemovedFalse(existingGroup.getGroupId(), userId)
					.orElseThrow(() -> new CommonException(FailureConstants.USER_NOT_MAPPED_TO_GROUP.getFailureCode(),
							FailureConstants.USER_NOT_MAPPED_TO_GROUP.getFailureMsg()));

			userGroupMapping.setIsRemoved(true);
			userGroupMapping.setModifiedTimestamp(new Date());

			userGroupMappingRepository.save(userGroupMapping);
		} catch (Exception e) {
			LOGGER.error("Exception in removeUserFromGroup", e);
			throw new CommonException(FailureConstants.REMOVE_USER_FROM_GROUP_ERROR.getFailureCode(),
					FailureConstants.REMOVE_USER_FROM_GROUP_ERROR.getFailureMsg());
		}
	}

	private void createUserGroupMapping(Long groupId, Long userId) {
		UserGroupMapping userGroupMapping = UserGroupMapping.builder().userId(userId).groupId(groupId).isRemoved(false)
				.build();
		userGroupMapping.setCreatedTimestamp(new Date());
		userGroupMapping.setModifiedTimestamp(new Date());

		userGroupMappingRepository.save(userGroupMapping);
	}

}
