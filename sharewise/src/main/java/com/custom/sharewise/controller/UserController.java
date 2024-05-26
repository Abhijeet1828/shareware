package com.custom.sharewise.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.response.CommonResponse;
import com.custom.common.utilities.response.ResponseHelper;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.FailureConstants;
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
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		Object response = userService.updateUser(updateUserRequest, userDetails);
		if (response instanceof Integer) {
			return ResponseHelper
					.generateResponse(
							new CommonResponse(FailureConstants.UPDATE_USER_ERROR.getFailureCode(),
									FailureConstants.UPDATE_USER_ERROR.getFailureMsg()),
							HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseHelper.generateResponse(SuccessConstants.UPDATE_USER.getSuccessCode(),
				SuccessConstants.UPDATE_USER.getSuccessMsg(), response);
	}

	@PutMapping(value = "/update-password")
	public ResponseEntity<Object> updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		Integer response = userService.updatePassword(updatePasswordRequest, userDetails);

		switch (response) {
		case 0:
			return ResponseHelper.generateResponse(FailureConstants.PASSWORDS_DO_NOT_MATCH.getFailureCode(),
					FailureConstants.PASSWORDS_DO_NOT_MATCH.getFailureMsg(), HttpStatus.UNAUTHORIZED);
		case 1:
			return ResponseHelper.generateResponse(SuccessConstants.UPDATE_PASSWORD.getSuccessCode(),
					SuccessConstants.UPDATE_PASSWORD.getSuccessMsg());
		default:
			return ResponseHelper.generateResponse(FailureConstants.INTERNAL_SERVER_ERROR.getFailureCode(),
					FailureConstants.INTERNAL_SERVER_ERROR.getFailureMsg(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
