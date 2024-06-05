package com.custom.sharewise.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.dto.UserDto;
import com.custom.sharewise.dto.UserGroupDto;
import com.custom.sharewise.model.UserGroupMapping;
import com.custom.sharewise.repository.UserGroupMappingRepository;
import com.custom.sharewise.response.GroupMembersResponse;
import com.custom.sharewise.validation.BusinessValidationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { CommonException.class,
		UnauthorizedException.class }, transactionManager = "transactionManager")
public class UserGroupMappingServiceImpl implements UserGroupMappingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupMappingServiceImpl.class);

	private final UserGroupMappingRepository userGroupMappingRepository;
	private final BusinessValidationService businessValidationService;
	private final UserService userService;

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
	public void addUserToGroup(Long groupId, Long userId, CustomUserDetails userDetails)
			throws CommonException, UnauthorizedException {
		try {
			Map<String, Object> validations = new HashMap<>();
			validations.put(Constants.VALIDATION_USER_ID, userId);
			validations.put(Constants.VALIDATION_GROUP_ID, groupId);
			validations.put(Constants.VALIDATION_GROUP_ADMIN, new UserGroupDto(groupId, userDetails.getUserId(), null));

			businessValidationService.validate(validations);

			Optional<UserGroupMapping> optUserGroupMapping = userGroupMappingRepository
					.findFirstByGroupIdAndUserId(groupId, userId);
			if (optUserGroupMapping.isPresent()) {
				UserGroupMapping existingUserGroupMapping = optUserGroupMapping.get();
				existingUserGroupMapping.setIsRemoved(false);
				existingUserGroupMapping.setModifiedTimestamp(new Date());

				userGroupMappingRepository.save(existingUserGroupMapping);
			} else {
				createUserGroupMapping(groupId, userId);
			}
		} catch (Exception e) {
			LOGGER.error("Exception in addUserToGroup", e);
			switch (e) {
			case CommonException ce -> throw ce;
			case UnauthorizedException au -> throw au;
			default -> throw new CommonException(FailureConstants.ADD_USER_TO_GROUP_ERROR.getFailureCode(),
					FailureConstants.ADD_USER_TO_GROUP_ERROR.getFailureMsg());
			}
		}
	}

	@Override
	public void removeUserFromGroup(Long groupId, Long userId, CustomUserDetails userDetails)
			throws CommonException, UnauthorizedException {
		try {
			Map<String, Object> validations = new HashMap<>();
			validations.put(Constants.VALIDATION_USER_ID, userId);
			validations.put(Constants.VALIDATION_GROUP_ID, groupId);
			validations.put(Constants.VALIDATION_GROUP_ADMIN, new UserGroupDto(groupId, userDetails.getUserId(), null));

			businessValidationService.validate(validations);

			UserGroupMapping userGroupMapping = userGroupMappingRepository
					.findFirstByGroupIdAndUserIdAndIsRemovedFalse(groupId, userId)
					.orElseThrow(() -> new CommonException(FailureConstants.USER_NOT_MAPPED_TO_GROUP.getFailureCode(),
							FailureConstants.USER_NOT_MAPPED_TO_GROUP.getFailureMsg()));

			userGroupMapping.setIsRemoved(true);
			userGroupMapping.setModifiedTimestamp(new Date());

			userGroupMappingRepository.save(userGroupMapping);
		} catch (Exception e) {
			LOGGER.error("Exception in removeUserFromGroup", e);
			switch (e) {
			case CommonException ce -> throw ce;
			case UnauthorizedException au -> throw au;
			default -> throw new CommonException(FailureConstants.REMOVE_USER_FROM_GROUP_ERROR.getFailureCode(),
					FailureConstants.REMOVE_USER_FROM_GROUP_ERROR.getFailureMsg());
			}
		}
	}

	@Override
	public Object fetchGroupMembers(Long groupId, CustomUserDetails userDetails) throws CommonException {
		try {
			businessValidationService.validate(Map.of(Constants.VALIDATION_USER_GROUP,
					new UserGroupDto(groupId, null, List.of(userDetails.getUserId()))));

			List<UserGroupMapping> groupMembers = userGroupMappingRepository.findByGroupIdAndIsRemovedFalse(groupId);

			Map<Long, UserDto> userMap = userService
					.findUsersById(groupMembers.stream().map(UserGroupMapping::getUserId).collect(Collectors.toSet()));
			return new GroupMembersResponse(groupId, userMap.values().stream().toList());
		} catch (Exception e) {
			LOGGER.error("Exception in getMembersOfGroup", e);
			if (e instanceof CommonException ce)
				throw ce;
			else
				throw new CommonException(FailureConstants.FETCH_GROUP_MEMBERS_ERROR.getFailureCode(),
						FailureConstants.FETCH_GROUP_MEMBERS_ERROR.getFailureMsg());
		}
	}

	@Override
	public Map<Long, UserDto> fetchGroupMembers(Long groupId) throws CommonException {
		try {
			List<UserGroupMapping> groupMembers = userGroupMappingRepository.findByGroupIdAndIsRemovedFalse(groupId);

			return userService
					.findUsersById(groupMembers.stream().map(UserGroupMapping::getUserId).collect(Collectors.toSet()));
		} catch (Exception e) {
			throw new CommonException(FailureConstants.FETCH_GROUP_MEMBERS_ERROR.getFailureCode(),
					FailureConstants.FETCH_GROUP_MEMBERS_ERROR.getFailureMsg());
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
