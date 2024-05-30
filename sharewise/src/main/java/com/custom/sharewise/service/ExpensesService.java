package com.custom.sharewise.service;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.request.AddExpenseRequest;

public interface ExpensesService {
	
	Object addExpense(AddExpenseRequest addExpenseRequest) throws CommonException;

}
