package com.custom.sharewise.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.response.UnifiedResponse;
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
import lombok.RequiredArgsConstructor;

/**
 * This controller is used for authentication purposes such as SignUp and Login.
 * 
 * @apiNote Request Mapping - /sharewise/auth/api/v1
 * @version V1
 * 
 * @author Abhijeet
 *
 */
@Tag(name = "Authentication Controller", description = "APIs for user sign-up and login")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = Constants.AUTHENTICATION_CONTROLLER + Constants.API_VERSION_1)
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@Operation(summary = "User Sign-Up API", description = "The API creates a user with the given details")
	@ApiResponse(responseCode = "201", description = "User created successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "409", description = "User with the email already exists", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@PostMapping(value = "/sign-up")
	public ResponseEntity<UnifiedResponse<User>> userSignUp(@RequestBody @Valid SignUpRequest signUpRequest) {
		User createdUser = authenticationService.userSignUp(signUpRequest);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.USER_SIGN_UP.getSuccessCode(),
				SuccessConstants.USER_SIGN_UP.getSuccessMsg(), createdUser), HttpStatus.CREATED);
	}

	@Operation(summary = "User Login API", description = "The API logs in a user and provides token for futher interaction")
	@ApiResponse(responseCode = "200", description = "Logged in successfully", useReturnTypeSchema = true)
	@PostMapping(value = "/login")
	public ResponseEntity<UnifiedResponse<LoginResponse>> userLogin(@RequestBody @Valid LoginRequest loginRequest) {
		LoginResponse loginResponse = authenticationService.userLogin(loginRequest);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.USER_LOGIN.getSuccessCode(),
				SuccessConstants.USER_LOGIN.getSuccessMsg(), loginResponse), HttpStatus.OK);
	}
}
