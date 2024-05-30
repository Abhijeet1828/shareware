package com.custom.sharewise.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
import com.custom.common.utilities.response.ResponseHelper;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.SuccessConstants;
import com.custom.sharewise.request.AddOrRemoveMemberRequest;
import com.custom.sharewise.service.UserGroupMappingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = Constants.GROUP_MEMBER_CONTROLLER + Constants.API_VERSION_1)
public class GroupMemberController {

	private final UserGroupMappingService userGroupMappingService;

	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/add")
	public ResponseEntity<Object> addMemberToGroup(@RequestBody @Valid AddOrRemoveMemberRequest addMemberRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException, UnauthorizedException {
		userGroupMappingService.addUserToGroup(addMemberRequest.getGroupId(), addMemberRequest.getUserId(),
				userDetails);

		return ResponseHelper.generateResponse(SuccessConstants.ADD_MEMBER_TO_GROUP.getSuccessCode(),
				SuccessConstants.ADD_MEMBER_TO_GROUP.getSuccessMsg());
	}

	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/remove")
	public ResponseEntity<Object> removeMemberFromGroup(
			@RequestBody @Valid AddOrRemoveMemberRequest removeMemberRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException, UnauthorizedException {
		userGroupMappingService.removeUserFromGroup(removeMemberRequest.getGroupId(), removeMemberRequest.getUserId(),
				userDetails);
		return ResponseHelper.generateResponse(SuccessConstants.REMOVE_MEMBER_FROM_GROUP.getSuccessCode(),
				SuccessConstants.REMOVE_MEMBER_FROM_GROUP.getSuccessMsg());
	}

}
