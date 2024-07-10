package com.custom.sharewise.service;

import java.util.Map;

import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.dto.UserDto;
import com.custom.sharewise.response.GroupMembersResponse;

public interface UserGroupMappingService {

	void addUserToGroup(Long groupId, Long userId);

	void addUserToGroup(Long groupId, Long userId, CustomUserDetails userDetails);

	void removeUserFromGroup(Long groupId, Long userId, CustomUserDetails userDetails);

	GroupMembersResponse fetchGroupMembers(Long groupId, CustomUserDetails userDetails);

	Map<Long, UserDto> fetchGroupMembers(Long groupId);

}
