package com.custom.sharewise.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.response.UnifiedResponse;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.SuccessConstants;
import com.custom.sharewise.response.GroupExpenseSummaryResponse;
import com.custom.sharewise.response.TotalGroupExpenseSummaryResponse;
import com.custom.sharewise.service.SummaryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@Tag(name = "Summary Controller", description = "Provides API for summarizing the group expenses and total expenses.")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = Constants.SUMMARY_CONTROLLER + Constants.API_VERSION_1)
public class SummaryController {

	private final SummaryService summaryService;

	@Operation(summary = "Summarize group expenses", description = "This API provides a list of all the group expenses and payments added for a group")
	@ApiResponse(responseCode = "200", description = "Group expenses fetched successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "401", description = "User not mapped to the group", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@Parameter(name = "groupId", description = "Group identifier", required = true)
	@GetMapping(value = "/group-expenses/{groupId}")
	public ResponseEntity<UnifiedResponse<GroupExpenseSummaryResponse>> fetchGroupExpenseSummary(
			@NotNull @Positive @PathVariable(value = "groupId", required = true) Long groupId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		GroupExpenseSummaryResponse groupExpenseSummaryResponse = summaryService.fetchGroupExpenseSummary(groupId,
				userDetails);

		return new ResponseEntity<>(
				new UnifiedResponse<>(SuccessConstants.FETCH_GROUP_EXPENSE_SUMMARY.getSuccessCode(),
						SuccessConstants.FETCH_GROUP_EXPENSE_SUMMARY.getSuccessMsg(), groupExpenseSummaryResponse),
				HttpStatus.OK);
	}

	@Operation(summary = "Summarize total group expenses", description = "This API provides a list of total group expenses by each person and category")
	@ApiResponse(responseCode = "200", description = "Total group expenses fetched successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "401", description = "User not mapped to the group", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@GetMapping(value = "/total-expense/{groupId}")
	public ResponseEntity<UnifiedResponse<TotalGroupExpenseSummaryResponse>> totalGroupExpense(
			@NotNull @Positive @PathVariable(value = "groupId", required = true) Long groupId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		TotalGroupExpenseSummaryResponse totalGroupExpenseSummaryResponse = summaryService
				.fetchTotalGroupExpense(groupId, userDetails);

		return new ResponseEntity<>(
				new UnifiedResponse<>(SuccessConstants.FETCH_TOTAL_GROUP_EXPENSE_SUMMARY.getSuccessCode(),
						SuccessConstants.FETCH_TOTAL_GROUP_EXPENSE_SUMMARY.getSuccessMsg(),
						totalGroupExpenseSummaryResponse),
				HttpStatus.OK);
	}
}
