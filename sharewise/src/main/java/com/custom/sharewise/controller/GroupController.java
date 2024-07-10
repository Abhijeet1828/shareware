package com.custom.sharewise.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.response.UnifiedResponse;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.SuccessConstants;
import com.custom.sharewise.model.Group;
import com.custom.sharewise.request.CreateOrUpdateGroupRequest;
import com.custom.sharewise.service.GroupService;
import com.custom.sharewise.validation.OnCreate;
import com.custom.sharewise.validation.OnUpdate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@Tag(name = "Group Controller", description = "CRUD APIs for groups")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = Constants.GROUP_CONTROLLER + Constants.API_VERSION_1)
public class GroupController {

	private final GroupService groupService;

	@Operation(summary = "Creates a group", description = "This API creates a group and assigns ROLE_ADMIN to the user creating the group.")
	@ApiResponse(responseCode = "201", description = "Group successfully created", useReturnTypeSchema = true)
	@Validated(value = OnCreate.class)
	@PostMapping(value = "/create")
	public ResponseEntity<UnifiedResponse<Group>> createGroup(
			@RequestBody @Valid CreateOrUpdateGroupRequest createGroupRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		Group group = groupService.createGroup(createGroupRequest, userDetails);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.CREATE_GROUP.getSuccessCode(),
				SuccessConstants.CREATE_GROUP.getSuccessMsg(), group), HttpStatus.CREATED);
	}

	@Operation(summary = "Updates group's details", description = "This API updates details of a group")
	@ApiResponse(responseCode = "200", description = "Group updated successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "Group not found", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@ApiResponse(responseCode = "401", description = "User is not group admin", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@Validated(value = OnUpdate.class)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@PutMapping(value = "/update")
	public ResponseEntity<UnifiedResponse<Group>> updateGroup(
			@RequestBody @Valid CreateOrUpdateGroupRequest updateGroupRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		Group group = groupService.updateGroup(updateGroupRequest, userDetails);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.UPDATE_GROUP.getSuccessCode(),
				SuccessConstants.UPDATE_GROUP.getSuccessMsg(), group), HttpStatus.OK);
	}

	@Operation(summary = "Deletes a group", description = "This API deletes a group")
	@ApiResponse(responseCode = "200", description = "Group deleted successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "Group not found", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@ApiResponse(responseCode = "401", description = "User is not group admin", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<UnifiedResponse<Void>> deleteGroup(
			@NotNull @Positive @PathVariable(value = "id", required = true) Long groupId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		groupService.deleteGroup(groupId, userDetails);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.DELETE_GROUP.getSuccessCode(),
				SuccessConstants.DELETE_GROUP.getSuccessMsg(), null), HttpStatus.OK);
	}

}
