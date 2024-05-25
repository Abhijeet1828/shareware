package com.custom.sharewise.service;

import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.request.UpdateUserRequest;

public interface UserService {

	public Object updateUser(UpdateUserRequest updateUserRequest, CustomUserDetails userDetails);

}
