package com.custom.sharewise.controller;

import java.util.Objects;

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
import com.custom.sharewise.response.GroupPaymentSummaryResponse;
import com.custom.sharewise.response.SimplifiedDebtResponse;
import com.custom.sharewise.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@Tag(name = "Payment Controller", description = "APIs to simplify/summarize group payments")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = Constants.PAYMENT_CONTROLLER + Constants.API_VERSION_1)
public class PaymentController {

	private final PaymentService paymentService;

	@Operation(summary = "Simplifies the group payments", description = "This API simplifies the group payments and displays simplified debt")
	@ApiResponse(responseCode = "200", description = "Successfully fetched simplified debt/No Debts to simplify", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "401", description = "User not mapped to the group", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@Parameter(name = "groupId", description = "Group identifier", required = true)
	@GetMapping(value = "/simplify/{groupId}")
	public ResponseEntity<UnifiedResponse<SimplifiedDebtResponse>> simplifyPayments(
			@NotNull @Positive @PathVariable(value = "groupId") Long groupId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		SimplifiedDebtResponse simplifiedDebtResponse = paymentService.simplifyPayments(groupId, userDetails);
		if (Objects.isNull(simplifiedDebtResponse))
			return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.NO_DEBTS_TO_SIMPLIFY.getSuccessCode(),
					SuccessConstants.NO_DEBTS_TO_SIMPLIFY.getSuccessMsg(), null), HttpStatus.OK);
		else
			return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.SIMPLIFY_PAYMENTS.getSuccessCode(),
					SuccessConstants.SIMPLIFY_PAYMENTS.getSuccessMsg(), simplifiedDebtResponse), HttpStatus.OK);
	}

	@Operation(summary = "Summarizes the group payments", description = "This API displays the total amount each user owes or is owed.")
	@ApiResponse(responseCode = "200", description = "Successfully summarized group payments", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "401", description = "User not mapped to the group", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@GetMapping(value = "/summary/{groupId}")
	public ResponseEntity<UnifiedResponse<GroupPaymentSummaryResponse>> summaryPayments(
			@NotNull @Positive @PathVariable(value = "groupId") Long groupId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		GroupPaymentSummaryResponse groupPaymentSummaryResponse = paymentService.paymentSummary(groupId, userDetails);

		return new ResponseEntity<>(
				new UnifiedResponse<>(SuccessConstants.FETCH_PAYMENT_SUMMARY.getSuccessCode(),
						SuccessConstants.FETCH_PAYMENT_SUMMARY.getSuccessMsg(), groupPaymentSummaryResponse),
				HttpStatus.OK);
	}
}
