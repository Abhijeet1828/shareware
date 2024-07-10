package com.custom.sharewise.service;

import java.util.List;

import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.model.GroupTransactions;
import com.custom.sharewise.request.AddOrUpdateExpenseRequest;
import com.custom.sharewise.request.AddOrUpdateTransactionRequest;

public interface GroupTransactionsService {

	void addGroupTransaction(AddOrUpdateExpenseRequest addExpenseRequest, Long groupExpensesId);

	GroupTransactions addUserPaymentTransaction(AddOrUpdateTransactionRequest addTransactionRequest,
			CustomUserDetails userDetails);

	GroupTransactions updateUserPaymentTransaction(AddOrUpdateTransactionRequest updateTransactionRequest,
			CustomUserDetails userDetails);

	GroupTransactions softDeleteUserPaymentTransaction(Long groupTransactionsId, CustomUserDetails userDetails);

	GroupTransactions restoreUserPaymentTransaction(Long groupTransactionsId, CustomUserDetails userDetails);

	void removeGroupTransactions(Long groupExpensesId);

	void softDeleteGroupTransactions(Long groupExpensesId);

	void restoreGroupTransaction(Long groupExpensesId);

	List<GroupTransactions> fetchAllGroupTransactionsByType(Long groupId, String transactionType);

}
