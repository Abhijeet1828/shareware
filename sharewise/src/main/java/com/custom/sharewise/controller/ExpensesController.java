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
import com.custom.sharewise.request.AddOrUpdateExpenseRequest;
import com.custom.sharewise.service.ExpensesService;
import com.custom.sharewise.validation.OnCreate;
import com.custom.sharewise.validation.OnUpdate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = Constants.EXPENSES_CONTROLLER + Constants.API_VERSION_1)
public class ExpensesController {

	private final ExpensesService expensesService;

	@Validated(value = OnCreate.class)
	@PostMapping(value = "/add")
	public ResponseEntity<Object> addExpense(@RequestBody @Valid AddOrUpdateExpenseRequest addExpenseRequest)
			throws CommonException {
		Object response = expensesService.addExpense(addExpenseRequest);

		return ResponseHelper.generateResponse(SuccessConstants.ADD_GROUP_EXPENSE.getSuccessCode(),
				SuccessConstants.ADD_GROUP_EXPENSE.getSuccessMsg(), response);
	}

	@Validated(value = OnUpdate.class)
	@PutMapping(value = "/update")
	public ResponseEntity<Object> updateExpense(@RequestBody @Valid AddOrUpdateExpenseRequest updateExpenseRequest)
			throws CommonException {
		Object response = expensesService.updateExpense(updateExpenseRequest);

		return ResponseHelper.generateResponse(SuccessConstants.UPDATE_GROUP_EXPENSE.getSuccessCode(),
				SuccessConstants.UPDATE_GROUP_EXPENSE.getSuccessMsg(), response);
	}

	@DeleteMapping(value = "/delete/{groupExpensesId}")
	public ResponseEntity<Object> deleteExpense(
			@NotNull @Positive @PathVariable(value = "groupExpensesId", required = true) Long groupExpensesId,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException {

		Object response = expensesService.deleteExpense(groupExpensesId, userDetails);

		return ResponseHelper.generateResponse(SuccessConstants.DELETE_GROUP_EXPENSE.getSuccessCode(),
				SuccessConstants.DELETE_GROUP_EXPENSE.getSuccessMsg(), response);
	}

	@PutMapping(value = "/restore/{groupExpensesId}")
	public ResponseEntity<Object> restoreExpense(
			@NotNull @Positive @PathVariable(value = "groupExpensesId", required = true) Long groupExpensesId,
			@AuthenticationPrincipal CustomUserDetails userDetails) throws CommonException {

		Object response = expensesService.restoreExpense(groupExpensesId, userDetails);

		return ResponseHelper.generateResponse(SuccessConstants.RESTORE_GROUP_EXPENSE.getSuccessCode(),
				SuccessConstants.RESTORE_GROUP_EXPENSE.getSuccessMsg(), response);
	}
}
