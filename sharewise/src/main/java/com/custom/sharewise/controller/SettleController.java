package com.custom.sharewise.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.service.SettleService;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = Constants.SETTLE_CONTROLLER + Constants.API_VERSION_1)
public class SettleController {

	private final SettleService settleService;

	@GetMapping(value = "/simplify/{groupId}")
	public ResponseEntity<Object> settleSimplify(@NotNull @Positive @PathVariable(value = "groupId") Long groupId)
			throws CommonException {
		settleService.settleSimplify(groupId);
		return null;
	}
}
