package com.custom.sharewise.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional(propagation = Propagation.REQUIRED, transactionManager = "transactionManager")
public class UserGroupMappingServiceImpl implements UserGroupMappingService {

	private final UserGroupMappingRepository userGroupMappingRepository;
	private final BusinessValidationService businessValidationService;
	private final UserService userService;

	@Override
	public void addUserToGroup(Long groupId, Long userId) {
		createUserGroupMapping(groupId, userId);
	}

	@Override
	public void addUserToGroup(Long groupId, Long userId, CustomUserDetails userDetails) {
		Map<String, Object> validations = new HashMap<>();
		validations.put(Constants.VALIDATION_USER_ID, userId);
		validations.put(Constants.VALIDATION_GROUP_ID, groupId);
		validations.put(Constants.VALIDATION_GROUP_ADMIN, new UserGroupDto(groupId, userDetails.getUserId(), null));

		businessValidationService.validate(validations);

		Optional<UserGroupMapping> optUserGroupMapping = userGroupMappingRepository.findFirstByGroupIdAndUserId(groupId,
				userId);
		if (optUserGroupMapping.isPresent()) {
			UserGroupMapping existingUserGroupMapping = optUserGroupMapping.get();
			existingUserGroupMapping.setIsRemoved(false);
			existingUserGroupMapping.setModifiedTimestamp(new Date());

			userGroupMappingRepository.save(existingUserGroupMapping);
		} else {
			createUserGroupMapping(groupId, userId);
		}
	}

	@Override
	public void removeUserFromGroup(Long groupId, Long userId, CustomUserDetails userDetails) {
		Map<String, Object> validations = new HashMap<>();
		validations.put(Constants.VALIDATION_USER_ID, userId);
		validations.put(Constants.VALIDATION_GROUP_ID, groupId);
		validations.put(Constants.VALIDATION_GROUP_ADMIN, new UserGroupDto(groupId, userDetails.getUserId(), null));

		businessValidationService.validate(validations);

		UserGroupMapping userGroupMapping = userGroupMappingRepository
				.findFirstByGroupIdAndUserIdAndIsRemovedFalse(groupId, userId)
				.orElseThrow(() -> new UnauthorizedException(FailureConstants.USER_NOT_MAPPED_TO_GROUP.getFailureCode(),
						FailureConstants.USER_NOT_MAPPED_TO_GROUP.getFailureMsg()));

		userGroupMapping.setIsRemoved(true);
		userGroupMapping.setModifiedTimestamp(new Date());

		userGroupMappingRepository.save(userGroupMapping);
	}

	@Override
	public GroupMembersResponse fetchGroupMembers(Long groupId, CustomUserDetails userDetails) {
		businessValidationService.validate(Map.of(Constants.VALIDATION_USER_GROUP,
				new UserGroupDto(groupId, null, List.of(userDetails.getUserId()))));

		List<UserGroupMapping> groupMembers = userGroupMappingRepository.findByGroupIdAndIsRemovedFalse(groupId);

		Map<Long, UserDto> userMap = userService
				.findUsersById(groupMembers.stream().map(UserGroupMapping::getUserId).collect(Collectors.toSet()));
		return new GroupMembersResponse(groupId, userMap.values().stream().toList());
	}

	@Override
	public Map<Long, UserDto> fetchGroupMembers(Long groupId) {
		List<UserGroupMapping> groupMembers = userGroupMappingRepository.findByGroupIdAndIsRemovedFalse(groupId);

		return userService
				.findUsersById(groupMembers.stream().map(UserGroupMapping::getUserId).collect(Collectors.toSet()));
	}

	private void createUserGroupMapping(Long groupId, Long userId) {
		UserGroupMapping userGroupMapping = UserGroupMapping.builder().userId(userId).groupId(groupId).isRemoved(false)
				.build();
		userGroupMapping.setCreatedTimestamp(new Date());
		userGroupMapping.setModifiedTimestamp(new Date());

		userGroupMappingRepository.save(userGroupMapping);
	}

}
