package com.custom.sharewise.service;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.request.AddOrUpdateExpenseRequest;
import com.custom.sharewise.request.AddOrUpdateTransactionRequest;

public interface GroupTransactionsService {

	void addGroupTransaction(AddOrUpdateExpenseRequest addExpenseRequest, Long groupExpensesId) throws CommonException;

	Object addUserPaymentTransaction(AddOrUpdateTransactionRequest addTransactionRequest, CustomUserDetails userDetails)
			throws CommonException;

	Object updateUserPaymentTransaction(AddOrUpdateTransactionRequest updateTransactionRequest,
			CustomUserDetails userDetails) throws CommonException;

	void removeGroupTransactions(Long groupExpensesId) throws CommonException;

	void softDeleteGroupTransactions(Long groupExpensesId) throws CommonException;

	void restoreGroupTransaction(Long groupExpensesId) throws CommonException;

}
