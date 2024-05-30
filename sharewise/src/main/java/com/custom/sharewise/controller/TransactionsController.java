package com.custom.sharewise.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.response.ResponseHelper;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.SuccessConstants;
import com.custom.sharewise.request.AddTransactionRequest;
import com.custom.sharewise.service.GroupTransactionsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = Constants.TRANSACTIONS_CONTROLLER + Constants.API_VERSION_1)
public class TransactionsController {

	private final GroupTransactionsService groupTransactionsService;

	@PostMapping(value = "/add")
	public ResponseEntity<Object> addTransaction(@RequestBody @Valid AddTransactionRequest addTransactionRequest)
			throws CommonException {
		Object response = groupTransactionsService.addUserPaymentTransaction(addTransactionRequest);

		return ResponseHelper.generateResponse(SuccessConstants.ADD_USER_TRANSACTION.getSuccessCode(),
				SuccessConstants.ADD_USER_TRANSACTION.getSuccessMsg(), response);
	}

}
