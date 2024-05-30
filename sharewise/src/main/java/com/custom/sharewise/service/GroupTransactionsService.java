package com.custom.sharewise.service;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.request.AddExpenseRequest;
import com.custom.sharewise.request.AddTransactionRequest;

public interface GroupTransactionsService {
	
	void addGroupTransaction(AddExpenseRequest addExpenseRequest, Long groupExpensesId) throws CommonException;
	
	Object addUserPaymentTransaction(AddTransactionRequest addTransactionRequest) throws CommonException;

}
