package com.custom.sharewise.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
import com.custom.common.utilities.response.ResponseHelper;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.SuccessConstants;
import com.custom.sharewise.request.UpdatePasswordRequest;
import com.custom.sharewise.request.UpdateUserRequest;
import com.custom.sharewise.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = Constants.USER_CONTROLLER + Constants.API_VERSION_1)
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PutMapping(value = "/update")
	public ResponseEntity<Object> updateUser(@RequestBody @Valid UpdateUserRequest updateUserRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException {
		Object response = userService.updateUser(updateUserRequest, userDetails);

		return ResponseHelper.generateResponse(SuccessConstants.UPDATE_USER.getSuccessCode(),
				SuccessConstants.UPDATE_USER.getSuccessMsg(), response);
	}

	@PutMapping(value = "/update-password")
	public ResponseEntity<Object> updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException, UnauthorizedException {
		userService.updatePassword(updatePasswordRequest, userDetails);

		return ResponseHelper.generateResponse(SuccessConstants.UPDATE_PASSWORD.getSuccessCode(),
				SuccessConstants.UPDATE_PASSWORD.getSuccessMsg());

	}

}
