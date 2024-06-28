package com.custom.sharewise.service;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.authentication.CustomUserDetails;

public interface SummaryService {

	Object fetchGroupExpenseSummary(Long groupId, CustomUserDetails userDetails) throws CommonException;

	Object fetchTotalGroupExpense(Long groupId, CustomUserDetails userDetails) throws CommonException;

}
