package com.custom.sharewise.service;

import java.math.BigDecimal;
import java.util.List;

import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.dto.GroupCategoryExpenseDto;
import com.custom.sharewise.model.GroupExpenses;
import com.custom.sharewise.request.AddOrUpdateExpenseRequest;

public interface ExpensesService {

	GroupExpenses addExpense(AddOrUpdateExpenseRequest addExpenseRequest);

	GroupExpenses updateExpense(AddOrUpdateExpenseRequest updateExpenseRequest);

	GroupExpenses deleteExpense(Long groupExpensesId, CustomUserDetails userDetails);

	GroupExpenses restoreExpense(Long groupExpensesId, CustomUserDetails userDetails);

	List<GroupExpenses> fetchAllGroupExpenses(Long groupId);

	BigDecimal fetchTotalGroupExpense(Long groupId);
	
	List<GroupCategoryExpenseDto> fetchTotalGroupExpenseByCategories(Long groupId);
}
