package com.custom.sharewise.service;

import java.util.Map;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.dto.UserDto;

public interface UserGroupMappingService {

	void addUserToGroup(Long groupId, Long userId) throws CommonException;

	void addUserToGroup(Long groupId, Long userId, CustomUserDetails userDetails)
			throws CommonException, UnauthorizedException;

	void removeUserFromGroup(Long groupId, Long userId, CustomUserDetails userDetails)
			throws CommonException, UnauthorizedException;

	Object fetchGroupMembers(Long groupId, CustomUserDetails userDetails) throws CommonException;

	Map<Long, UserDto> fetchGroupMembers(Long groupId) throws CommonException;

}
