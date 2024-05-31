package com.custom.sharewise.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentSummaryResponse {

	private List<UserDebts> paymentSummary;

	private boolean isSimplified;
}
