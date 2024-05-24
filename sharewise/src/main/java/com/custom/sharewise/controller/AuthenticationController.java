package com.custom.sharewise.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.response.CommonResponse;
import com.custom.common.utilities.response.ResponseHelper;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.constants.SuccessConstants;
import com.custom.sharewise.request.LoginRequest;
import com.custom.sharewise.request.SignUpRequest;
import com.custom.sharewise.service.AuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = Constants.AUTHENTICATION_CONTROLLER + Constants.API_VERSION_1)
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping(value = "/sign-up")
	public ResponseEntity<Object> userSignUp(@RequestBody @Valid SignUpRequest signUpRequest) {
		Object response = authenticationService.userSignUp(signUpRequest);

		if (response instanceof Integer i) {
			if (i == 1) {
				return ResponseHelper.generateResponse(
						new CommonResponse(FailureConstants.USER_ALREADY_EXISTS.getFailureCode(),
								FailureConstants.USER_ALREADY_EXISTS.getFailureMsg()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return ResponseHelper
						.generateResponse(
								new CommonResponse(FailureConstants.SIGN_UP_ERROR.getFailureCode(),
										FailureConstants.SIGN_UP_ERROR.getFailureMsg()),
								HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return ResponseHelper.generateResponse(SuccessConstants.USER_SIGN_UP.getSuccessCode(),
				SuccessConstants.USER_SIGN_UP.getSuccessMsg(), response);

	}

	@PostMapping(value = "/login")
	public ResponseEntity<Object> userLogin(@RequestBody @Valid LoginRequest loginRequest) {
		Object response = authenticationService.userLogin(loginRequest);

		if (response instanceof Integer i) {
			if (i == 1) {
				return ResponseHelper.generateResponse(new CommonResponse(FailureConstants.LOGIN_ERROR.getFailureCode(),
						FailureConstants.LOGIN_ERROR.getFailureMsg()), HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return ResponseHelper.generateResponse(
						new CommonResponse(FailureConstants.INTERNAL_SERVER_ERROR.getFailureCode(),
								FailureConstants.INTERNAL_SERVER_ERROR.getFailureMsg()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return ResponseHelper.generateResponse(SuccessConstants.USER_LOGIN.getSuccessCode(),
				SuccessConstants.USER_LOGIN.getSuccessMsg(), Map.of("token", response));
	}

}
