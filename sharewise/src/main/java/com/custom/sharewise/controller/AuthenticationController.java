package com.custom.sharewise.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.response.ResponseHelper;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.SuccessConstants;
import com.custom.sharewise.model.User;
import com.custom.sharewise.request.LoginRequest;
import com.custom.sharewise.request.SignUpRequest;
import com.custom.sharewise.response.LoginResponse;
import com.custom.sharewise.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * This controller is used for authentication purposes such as SignUp and Login.
 * 
 * @apiNote Request Mapping - /sharewise/auth/api/v1
 * @version V1
 * 
 * @author Abhijeet
 *
 */
@RestController
@RequestMapping(value = Constants.AUTHENTICATION_CONTROLLER + Constants.API_VERSION_1)
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Tag(name = "post", description = "POST methods of Authentication Controller")
	@Operation(summary = "User Sign-Up API", description = "The API creates a user with the given details")
	@ApiResponse(responseCode = "200", description = "User created successfully", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) })
	@PostMapping(value = "/sign-up")
	public ResponseEntity<Object> userSignUp(@RequestBody @Valid SignUpRequest signUpRequest) throws CommonException {
		Object response = authenticationService.userSignUp(signUpRequest);

		return ResponseHelper.generateResponse(SuccessConstants.USER_SIGN_UP.getSuccessCode(),
				SuccessConstants.USER_SIGN_UP.getSuccessMsg(), response);

	}

	@Tag(name = "post", description = "POST methods of Authentication Controller")
	@Operation(summary = "User Login API", description = "The API logs in a user and provides token for futher interaction")
	@ApiResponse(responseCode = "200", description = "Logged in successfully", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class)) })
	@PostMapping(value = "/login")
	public ResponseEntity<Object> userLogin(@RequestBody @Valid LoginRequest loginRequest) {
		Object response = authenticationService.userLogin(loginRequest);

		return ResponseHelper.generateResponse(SuccessConstants.USER_LOGIN.getSuccessCode(),
				SuccessConstants.USER_LOGIN.getSuccessMsg(),
				new LoginResponse(loginRequest.getEmail(), response.toString()));
	}

}
