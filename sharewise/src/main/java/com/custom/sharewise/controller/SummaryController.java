package com.custom.sharewise.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.response.ResponseHelper;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.SuccessConstants;
import com.custom.sharewise.service.SummaryService;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = Constants.SUMMARY_CONTROLLER + Constants.API_VERSION_1)
public class SummaryController {

	private final SummaryService summaryService;

	@GetMapping(value = "/group-expenses/{groupId}")
	public ResponseEntity<Object> fetchGroupExpenses(
			@NotNull @Positive @PathVariable(value = "groupId", required = true) Long groupId,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException {
		Object response = summaryService.fetchGroupExpenseSummary(groupId, userDetails);

		return ResponseHelper.generateResponse(SuccessConstants.FETCH_GROUP_EXPENSE_SUMMARY.getSuccessCode(),
				SuccessConstants.FETCH_GROUP_EXPENSE_SUMMARY.getSuccessMsg(), response);
	}
}
