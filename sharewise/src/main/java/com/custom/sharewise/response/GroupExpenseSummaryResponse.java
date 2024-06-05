package com.custom.sharewise.response;

import java.util.List;

import com.custom.sharewise.dto.ExpenseSummaryDto;

public record GroupExpenseSummaryResponse(Long groupId, List<ExpenseSummaryDto> expenses) {

}
