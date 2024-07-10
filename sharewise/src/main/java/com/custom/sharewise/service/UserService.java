package com.custom.sharewise.service;

import java.util.Map;
import java.util.Set;

import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.dto.UserDto;
import com.custom.sharewise.model.User;
import com.custom.sharewise.request.UpdatePasswordRequest;
import com.custom.sharewise.request.UpdateUserRequest;

public interface UserService {

	public User updateUser(UpdateUserRequest updateUserRequest, CustomUserDetails userDetails);

	public void updatePassword(UpdatePasswordRequest updatePasswordRequest, CustomUserDetails userDetails);

	public Map<Long, UserDto> findUsersById(Set<Long> userIds);

}
