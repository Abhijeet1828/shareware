package com.custom.sharewise.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.response.ResponseHelper;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.SuccessConstants;
import com.custom.sharewise.request.CreateGroupRequest;
import com.custom.sharewise.service.GroupService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = Constants.GROUP_CONTROLLER + Constants.API_VERSION_1)
public class GroupController {

	private final GroupService groupService;

	public GroupController(GroupService groupService) {
		this.groupService = groupService;
	}

	@PostMapping(value = "/create")
	public ResponseEntity<Object> createGroup(@RequestBody @Valid CreateGroupRequest createGroupRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException {
		Object response = groupService.createGroup(createGroupRequest, userDetails);

		return ResponseHelper.generateResponse(SuccessConstants.CREATE_GROUP.getSuccessCode(),
				SuccessConstants.CREATE_GROUP.getSuccessMsg(), response);
	} 
	
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/update")
	public ResponseEntity<Object> updateGroup() {
		return ResponseEntity.ok(null);
	}

}
