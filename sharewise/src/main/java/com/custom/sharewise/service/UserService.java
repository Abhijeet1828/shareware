package com.custom.sharewise.service;

import java.util.Map;
import java.util.Set;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.dto.UserDto;
import com.custom.sharewise.request.UpdatePasswordRequest;
import com.custom.sharewise.request.UpdateUserRequest;

public interface UserService {

	public Object updateUser(UpdateUserRequest updateUserRequest, CustomUserDetails userDetails) throws CommonException;

	public void updatePassword(UpdatePasswordRequest updatePasswordRequest, CustomUserDetails userDetails)
			throws CommonException, UnauthorizedException;

	public Map<Long, UserDto> findUsersById(Set<Long> userIds);

}
