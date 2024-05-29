package com.custom.sharewise.controller;

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

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
import com.custom.common.utilities.response.ResponseHelper;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.SuccessConstants;
import com.custom.sharewise.request.CreateOrUpdateGroupRequest;
import com.custom.sharewise.service.GroupService;
import com.custom.sharewise.validation.OnCreate;
import com.custom.sharewise.validation.OnUpdate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping(value = Constants.GROUP_CONTROLLER + Constants.API_VERSION_1)
public class GroupController {

	private final GroupService groupService;

	public GroupController(GroupService groupService) {
		this.groupService = groupService;
	}

	@Validated(value = OnCreate.class)
	@PostMapping(value = "/create")
	public ResponseEntity<Object> createGroup(@RequestBody @Valid CreateOrUpdateGroupRequest createGroupRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException {
		Object response = groupService.createGroup(createGroupRequest, userDetails);

		return ResponseHelper.generateResponse(SuccessConstants.CREATE_GROUP.getSuccessCode(),
				SuccessConstants.CREATE_GROUP.getSuccessMsg(), response);
	}

	@Validated(value = OnUpdate.class)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@PutMapping(value = "/update")
	public ResponseEntity<Object> updateGroup(@RequestBody @Valid CreateOrUpdateGroupRequest updateGroupRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException, UnauthorizedException {
		Object response = groupService.updateGroup(updateGroupRequest, userDetails);

		return ResponseHelper.generateResponse(SuccessConstants.UPDATE_GROUP.getSuccessCode(),
				SuccessConstants.UPDATE_GROUP.getSuccessMsg(), response);
	}

	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Object> deleteGroup(
			@NotNull @Positive @PathVariable(value = "id", required = true) Long groupId,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException, UnauthorizedException {
		groupService.deleteGroup(groupId, userDetails);

		return ResponseHelper.generateResponse(SuccessConstants.DELETE_GROUP.getSuccessCode(),
				SuccessConstants.DELETE_GROUP.getSuccessMsg());
	}

}
