package com.custom.sharewise.response;

import java.util.List;

import com.custom.sharewise.dto.UserPaymentSummary;

public record GroupPaymentSummaryResponse(Long groupId, List<UserPaymentSummary> paymentSummary) {

}
