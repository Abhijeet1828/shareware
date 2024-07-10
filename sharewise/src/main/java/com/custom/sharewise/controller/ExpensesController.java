package com.custom.sharewise.controller;

import org.springframework.http.HttpStatus;
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

import com.custom.common.utilities.response.UnifiedResponse;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.SuccessConstants;
import com.custom.sharewise.model.GroupExpenses;
import com.custom.sharewise.request.AddOrUpdateExpenseRequest;
import com.custom.sharewise.service.ExpensesService;
import com.custom.sharewise.validation.OnCreate;
import com.custom.sharewise.validation.OnUpdate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@Tag(name = "Expenses Controller", description = "CRUD APIs for user payments")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = Constants.EXPENSES_CONTROLLER + Constants.API_VERSION_1)
public class ExpensesController {

	private final ExpensesService expensesService;

	@Operation(summary = "Adding user payment", description = "This API adds a user payment i.e paying back what a person owes to someone.")
	@ApiResponse(responseCode = "201", description = "User payment added successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "Group not found", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@ApiResponse(responseCode = "401", description = "User not mapped to the group", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@Validated(value = OnCreate.class)
	@PostMapping(value = "/add")
	public ResponseEntity<UnifiedResponse<GroupExpenses>> addExpense(
			@RequestBody @Valid AddOrUpdateExpenseRequest addExpenseRequest) {
		GroupExpenses groupExpenses = expensesService.addExpense(addExpenseRequest);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.ADD_GROUP_EXPENSE.getSuccessCode(),
				SuccessConstants.ADD_GROUP_EXPENSE.getSuccessMsg(), groupExpenses), HttpStatus.CREATED);
	}

	@Operation(summary = "Updating user payment", description = "This API updates a user payment")
	@ApiResponse(responseCode = "200", description = "User payment updated successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "Group not found/Group expense not found", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@ApiResponse(responseCode = "401", description = "User not mapped to the group", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@Validated(value = OnUpdate.class)
	@PutMapping(value = "/update")
	public ResponseEntity<UnifiedResponse<GroupExpenses>> updateExpense(
			@RequestBody @Valid AddOrUpdateExpenseRequest updateExpenseRequest) {
		GroupExpenses groupExpenses = expensesService.updateExpense(updateExpenseRequest);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.UPDATE_GROUP_EXPENSE.getSuccessCode(),
				SuccessConstants.UPDATE_GROUP_EXPENSE.getSuccessMsg(), groupExpenses), HttpStatus.OK);
	}

	@Operation(summary = "Deleting user payment", description = "This API soft-deletes a user payment")
	@ApiResponse(responseCode = "200", description = "User payment deleted successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "Group expense not found", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@ApiResponse(responseCode = "401", description = "User not mapped to the group", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@Parameter(name = "groupExpensesId", description = "User Payment Identifier", required = true)
	@DeleteMapping(value = "/delete/{groupExpensesId}")
	public ResponseEntity<UnifiedResponse<GroupExpenses>> deleteExpense(
			@NotNull @Positive @PathVariable(value = "groupExpensesId", required = true) Long groupExpensesId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		GroupExpenses groupExpenses = expensesService.deleteExpense(groupExpensesId, userDetails);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.DELETE_GROUP_EXPENSE.getSuccessCode(),
				SuccessConstants.DELETE_GROUP_EXPENSE.getSuccessMsg(), groupExpenses), HttpStatus.OK);
	}

	@Operation(summary = "Restoring user payment", description = "This API restores the deleted user payment")
	@ApiResponse(responseCode = "200", description = "User payment restored successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "Group expense not found", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@ApiResponse(responseCode = "401", description = "User not mapped to the group", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@Parameter(name = "groupExpensesId", description = "User Payment Identifier", required = true)
	@PutMapping(value = "/restore/{groupExpensesId}")
	public ResponseEntity<UnifiedResponse<GroupExpenses>> restoreExpense(
			@NotNull @Positive @PathVariable(value = "groupExpensesId", required = true) Long groupExpensesId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		GroupExpenses groupExpenses = expensesService.restoreExpense(groupExpensesId, userDetails);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.RESTORE_GROUP_EXPENSE.getSuccessCode(),
				SuccessConstants.RESTORE_GROUP_EXPENSE.getSuccessMsg(), groupExpenses), HttpStatus.OK);
	}
}
