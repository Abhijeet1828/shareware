package com.custom.sharewise.service;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.request.UpdatePasswordRequest;
import com.custom.sharewise.request.UpdateUserRequest;

public interface UserService {

	public Object updateUser(UpdateUserRequest updateUserRequest, CustomUserDetails userDetails) throws CommonException;
	
	public Integer updatePassword(UpdatePasswordRequest updatePasswordRequest, CustomUserDetails userDetails) throws CommonException;

}
