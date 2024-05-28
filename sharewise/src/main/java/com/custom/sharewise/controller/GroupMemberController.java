package com.custom.sharewise.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.sharewise.constants.Constants;

@Validated
@RestController
@RequestMapping(value = Constants.GROUP_MEMBER_CONTROLLER + Constants.API_VERSION_1)
public class GroupMemberController {

	public GroupMemberController() {
		// TODO Auto-generated constructor stub
	}

	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/add-member")
	public ResponseEntity<Object> addMemberToGroup() {
		return null;
	}

	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/remove-member")
	public ResponseEntity<Object> removeMemberFromGroup() {
		return null;
	}

}
