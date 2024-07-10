package com.custom.sharewise.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.response.UnifiedResponse;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.SuccessConstants;
import com.custom.sharewise.model.User;
import com.custom.sharewise.request.UpdatePasswordRequest;
import com.custom.sharewise.request.UpdateUserRequest;
import com.custom.sharewise.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "User Controller", description = "APIs for updating user details and password")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = Constants.USER_CONTROLLER + Constants.API_VERSION_1)
public class UserController {

	private final UserService userService;

	@Operation(summary = "Updates user details", description = "This API is used to update the user details")
	@ApiResponse(responseCode = "200", description = "User details updated successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "User not found", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@PutMapping(value = "/update")
	public ResponseEntity<UnifiedResponse<User>> updateUser(@RequestBody @Valid UpdateUserRequest updateUserRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		User user = userService.updateUser(updateUserRequest, userDetails);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.UPDATE_USER.getSuccessCode(),
				SuccessConstants.UPDATE_USER.getSuccessMsg(), user), HttpStatus.OK);
	}

	@Operation(summary = "Updates user's pasword", description = "This API is used to update the user password")
	@ApiResponse(responseCode = "200", description = "User password updated successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "401", description = "Old password does not match", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@PutMapping(value = "/update-password")
	public ResponseEntity<UnifiedResponse<Void>> updatePassword(
			@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		userService.updatePassword(updatePasswordRequest, userDetails);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.UPDATE_PASSWORD.getSuccessCode(),
				SuccessConstants.UPDATE_PASSWORD.getSuccessMsg(), null), HttpStatus.OK);
	}

}
