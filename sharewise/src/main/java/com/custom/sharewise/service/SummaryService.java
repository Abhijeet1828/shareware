package com.custom.sharewise.service;

import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.response.GroupExpenseSummaryResponse;
import com.custom.sharewise.response.TotalGroupExpenseSummaryResponse;

public interface SummaryService {

	GroupExpenseSummaryResponse fetchGroupExpenseSummary(Long groupId, CustomUserDetails userDetails);

	TotalGroupExpenseSummaryResponse fetchTotalGroupExpense(Long groupId, CustomUserDetails userDetails);

}
