package com.custom.sharewise.service;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.request.AddOrUpdateExpenseRequest;
import com.custom.sharewise.request.AddTransactionRequest;

public interface GroupTransactionsService {
	
	void addGroupTransaction(AddOrUpdateExpenseRequest addExpenseRequest, Long groupExpensesId) throws CommonException;
	
	Object addUserPaymentTransaction(AddTransactionRequest addTransactionRequest) throws CommonException;
	
	void removeGroupTransactions(Long groupExpensesId) throws CommonException;

}
