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
import com.custom.sharewise.service.PaymentService;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = Constants.PAYMENT_CONTROLLER + Constants.API_VERSION_1)
public class PaymentController {

	private final PaymentService paymentService;

	@GetMapping(value = "/simplify/{groupId}")
	public ResponseEntity<Object> simplifyPayments(@NotNull @Positive @PathVariable(value = "groupId") Long groupId,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException {
		Object response = paymentService.simplifyPayments(groupId, userDetails);
		if (response instanceof Integer)
			return ResponseHelper.generateResponse(SuccessConstants.NO_DEBTS_TO_SIMPLIFY.getSuccessCode(),
					SuccessConstants.NO_DEBTS_TO_SIMPLIFY.getSuccessMsg());
		else
			return ResponseHelper.generateResponse(SuccessConstants.SIMPLIFY_PAYMENTS.getSuccessCode(),
					SuccessConstants.SIMPLIFY_PAYMENTS.getSuccessMsg(), response);
	}

	@GetMapping(value = "/summary/{groupId}")
	public ResponseEntity<Object> summaryPayments(@NotNull @Positive @PathVariable(value = "groupId") Long groupId,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException {
		Object response = paymentService.paymentSummary(groupId, userDetails);

		return ResponseHelper.generateResponse(SuccessConstants.FETCH_PAYMENT_SUMMARY.getSuccessCode(),
				SuccessConstants.FETCH_PAYMENT_SUMMARY.getSuccessMsg(), response);
	}
}
