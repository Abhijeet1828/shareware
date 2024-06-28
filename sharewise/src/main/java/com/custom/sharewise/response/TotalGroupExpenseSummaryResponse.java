package com.custom.sharewise.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.custom.sharewise.dto.GroupCategoryExpenseDto;

public record TotalGroupExpenseSummaryResponse(BigDecimal totalGroupExpenditure,
		List<GroupCategoryExpenseDto> groupCategoryExpenses, Map<String, BigDecimal> userExpenseMap) {

}
