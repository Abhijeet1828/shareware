package com.custom.sharewise.service;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
import com.custom.sharewise.authentication.CustomUserDetails;

public interface UserGroupMappingService {

	void addUserToGroup(Long groupId, Long userId) throws CommonException;

	void addUserToGroup(Long groupId, Long userId, CustomUserDetails userDetails)
			throws CommonException, UnauthorizedException;

	void removeUserFromGroup(Long groupId, Long userId, CustomUserDetails userDetails)
			throws CommonException, UnauthorizedException;

}
