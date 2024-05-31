package com.custom.sharewise.controller;

import org.springframework.http.ResponseEntity;
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
import com.custom.common.utilities.response.ResponseHelper;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.SuccessConstants;
import com.custom.sharewise.request.AddOrUpdateTransactionRequest;
import com.custom.sharewise.service.GroupTransactionsService;
import com.custom.sharewise.validation.OnCreate;
import com.custom.sharewise.validation.OnUpdate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = Constants.TRANSACTIONS_CONTROLLER + Constants.API_VERSION_1)
public class TransactionsController {

	private final GroupTransactionsService groupTransactionsService;

	@Validated(value = OnCreate.class)
	@PostMapping(value = "/add")
	public ResponseEntity<Object> addTransaction(
			@RequestBody @Valid AddOrUpdateTransactionRequest addTransactionRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException {
		Object response = groupTransactionsService.addUserPaymentTransaction(addTransactionRequest, userDetails);

		return ResponseHelper.generateResponse(SuccessConstants.ADD_USER_TRANSACTION.getSuccessCode(),
				SuccessConstants.ADD_USER_TRANSACTION.getSuccessMsg(), response);
	}

	@Validated(value = OnUpdate.class)
	@PutMapping(value = "/update")
	public ResponseEntity<Object> updateTransaction(
			@RequestBody @Valid AddOrUpdateTransactionRequest updateTransactionRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException {
		Object response = groupTransactionsService.updateUserPaymentTransaction(updateTransactionRequest, userDetails);

		return ResponseHelper.generateResponse(SuccessConstants.UPDATE_USER_TRANSACTION.getSuccessCode(),
				SuccessConstants.UPDATE_USER_TRANSACTION.getSuccessMsg(), response);
	}

	@DeleteMapping(value = "/delete/{groupTransactionsId}")
	public ResponseEntity<Object> deleteTransaction(
			@NotNull @Positive @PathVariable(value = "groupTransactionsId", required = true) Long groupTransactionsId,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException {
		Object response = groupTransactionsService.softDeleteUserPaymentTransaction(groupTransactionsId, userDetails);

		return ResponseHelper.generateResponse(SuccessConstants.DELETE_USER_TRANSACTION.getSuccessCode(),
				SuccessConstants.DELETE_USER_TRANSACTION.getSuccessMsg(), response);
	}

	@PutMapping(value = "/restore/{groupTransactionsId}")
	public ResponseEntity<Object> restoreTransaction(
			@NotNull @Positive @PathVariable(value = "groupTransactionsId", required = true) Long groupTransactionsId,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException {
		Object response = groupTransactionsService.restoreUserPaymentTransaction(groupTransactionsId, userDetails);

		return ResponseHelper.generateResponse(SuccessConstants.RESTORE_USER_TRANSACTION.getSuccessCode(),
				SuccessConstants.RESTORE_USER_TRANSACTION.getSuccessMsg(), response);
	}

}
