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
import com.custom.sharewise.model.GroupTransactions;
import com.custom.sharewise.request.AddOrUpdateTransactionRequest;
import com.custom.sharewise.service.GroupTransactionsService;
import com.custom.sharewise.validation.OnCreate;
import com.custom.sharewise.validation.OnUpdate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@Tag(name = "Transactions Controller", description = "CRUD APIs for user transactions")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = Constants.TRANSACTIONS_CONTROLLER + Constants.API_VERSION_1)
public class TransactionsController {

	private final GroupTransactionsService groupTransactionsService;

	@Operation(summary = "Adds a user transaction", description = "This API is used to add a new user transaction")
	@ApiResponse(responseCode = "201", description = "User transaction added successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "Group not found", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@ApiResponse(responseCode = "401", description = "User not mapped to the group", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@Validated(value = OnCreate.class)
	@PostMapping(value = "/add")
	public ResponseEntity<UnifiedResponse<GroupTransactions>> addTransaction(
			@RequestBody @Valid AddOrUpdateTransactionRequest addTransactionRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		GroupTransactions groupTransactions = groupTransactionsService.addUserPaymentTransaction(addTransactionRequest,
				userDetails);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.ADD_USER_TRANSACTION.getSuccessCode(),
				SuccessConstants.ADD_USER_TRANSACTION.getSuccessMsg(), groupTransactions), HttpStatus.CREATED);
	}

	@Operation(summary = "Updates a user transaction", description = "This API is used to update a user transaction")
	@ApiResponse(responseCode = "200", description = "User transaction updated successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "Group not found/Transaction not found", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@ApiResponse(responseCode = "401", description = "User not mapped to the group", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@Validated(value = OnUpdate.class)
	@PutMapping(value = "/update")
	public ResponseEntity<UnifiedResponse<GroupTransactions>> updateTransaction(
			@RequestBody @Valid AddOrUpdateTransactionRequest updateTransactionRequest,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		GroupTransactions groupTransactions = groupTransactionsService
				.updateUserPaymentTransaction(updateTransactionRequest, userDetails);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.UPDATE_USER_TRANSACTION.getSuccessCode(),
				SuccessConstants.UPDATE_USER_TRANSACTION.getSuccessMsg(), groupTransactions), HttpStatus.OK);
	}

	@Operation(summary = "Deletes a user transaction", description = "This API is used to delete a user transaction")
	@ApiResponse(responseCode = "200", description = "User transaction deleted successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "Group not found/Transaciton not found", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@ApiResponse(responseCode = "401", description = "User not mapped to the group", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@DeleteMapping(value = "/delete/{groupTransactionsId}")
	public ResponseEntity<UnifiedResponse<GroupTransactions>> deleteTransaction(
			@NotNull @Positive @PathVariable(value = "groupTransactionsId", required = true) Long groupTransactionsId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		GroupTransactions groupTransactions = groupTransactionsService
				.softDeleteUserPaymentTransaction(groupTransactionsId, userDetails);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.DELETE_USER_TRANSACTION.getSuccessCode(),
				SuccessConstants.DELETE_USER_TRANSACTION.getSuccessMsg(), groupTransactions), HttpStatus.OK);
	}

	@Operation(summary = "Restores a user transaction", description = "This API is used to restore a user transaction")
	@ApiResponse(responseCode = "200", description = "User transaction restored successfully", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "Group not found/Transaciton not found", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@ApiResponse(responseCode = "401", description = "User not mapped to the group", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UnifiedResponse.class)) })
	@PutMapping(value = "/restore/{groupTransactionsId}")
	public ResponseEntity<UnifiedResponse<GroupTransactions>> restoreTransaction(
			@NotNull @Positive @PathVariable(value = "groupTransactionsId", required = true) Long groupTransactionsId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		GroupTransactions groupTransactions = groupTransactionsService
				.restoreUserPaymentTransaction(groupTransactionsId, userDetails);

		return new ResponseEntity<>(new UnifiedResponse<>(SuccessConstants.RESTORE_USER_TRANSACTION.getSuccessCode(),
				SuccessConstants.RESTORE_USER_TRANSACTION.getSuccessMsg(), groupTransactions), HttpStatus.OK);
	}

}
