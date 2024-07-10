package com.custom.sharewise.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.response.UnifiedResponse;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.SuccessConstants;
import com.custom.sharewise.request.AddOrRemoveMemberRequest;
import com.custom.sharewise.response.GroupMembersResponse;
import com.custom.sharewise.service.UserGroupMappingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@Tag(name = "Group member Controller", description = "CRUD APIs for group members")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = Constants.GROUP_MEMBER_CONTROLLER + Constants.API_VERSION_1)
public class GroupMemberController {

	private final UserGroupMappingService userGroupMappingService;

	@Operation(summary = "Add a member to a group", description = "This API is used to add members to the provided group")
	@ApiResponse(responseCode = "201", description = "Group member added successfully", useReturnTypeSchema = true)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/add")
	public ResponseEntity<UnifiedResponse<Void>> addMemberToGroup(
			@RequestBody @Valid AddOrRemoveMemberRequest addMemberRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		userGroupMappingService.addUserToGroup(addMemberRequest.getGroupId(), addMemberRequest.getUserId(),
				userDetails);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.ADD_MEMBER_TO_GROUP.getSuccessCode(),
				SuccessConstants.ADD_MEMBER_TO_GROUP.getSuccessMsg(), null), HttpStatus.CREATED);
	}

	@Operation(summary = "Remove member from a group", description = "This API is used to remove member from the provided group")
	@ApiResponse(responseCode = "200", description = "Group member removed successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "User not found/Group not found", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@ApiResponse(responseCode = "401", description = "User not group admin/User not mapped to group", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/remove")
	public ResponseEntity<UnifiedResponse<Void>> removeMemberFromGroup(
			@RequestBody @Valid AddOrRemoveMemberRequest removeMemberRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		userGroupMappingService.removeUserFromGroup(removeMemberRequest.getGroupId(), removeMemberRequest.getUserId(),
				userDetails);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.REMOVE_MEMBER_FROM_GROUP.getSuccessCode(),
				SuccessConstants.REMOVE_MEMBER_FROM_GROUP.getSuccessMsg(), null), HttpStatus.OK);
	}

	@Operation(summary = "Fetch group members", description = "Fetches all the active group members")
	@ApiResponse(responseCode = "200", description = "Group members fetched successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "401", description = "User not mapped to group", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@Parameter(name = "groupId", description = "Group identifier", required = true)
	@GetMapping(value = "/fetch/{groupId}")
	public ResponseEntity<UnifiedResponse<GroupMembersResponse>> fetchGroupMembers(
			@NotNull @Positive @PathVariable(value = "groupId") Long groupId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		GroupMembersResponse groupMembersResponse = userGroupMappingService.fetchGroupMembers(groupId, userDetails);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.FETCH_GROUP_MEMBERS.getSuccessCode(),
				SuccessConstants.FETCH_GROUP_MEMBERS.getSuccessMsg(), groupMembersResponse), HttpStatus.OK);
	}

}
