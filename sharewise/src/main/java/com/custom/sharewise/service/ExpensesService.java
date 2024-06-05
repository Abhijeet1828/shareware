package com.custom.sharewise.service;

import java.util.List;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.model.GroupExpenses;
import com.custom.sharewise.request.AddOrUpdateExpenseRequest;

public interface ExpensesService {

	Object addExpense(AddOrUpdateExpenseRequest addExpenseRequest) throws CommonException;

	Object updateExpense(AddOrUpdateExpenseRequest updateExpenseRequest) throws CommonException;

	Object deleteExpense(Long groupExpensesId, CustomUserDetails userDetails) throws CommonException;

	Object restoreExpense(Long groupExpensesId, CustomUserDetails userDetails) throws CommonException;
	
	List<GroupExpenses> fetchAllGroupExpenses(Long groupId) throws CommonException;
}
