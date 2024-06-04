package com.custom.sharewise.response;

import java.util.List;

import com.custom.sharewise.dto.UserPayment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentSummaryResponse {

	private List<UserPayment> paymentSummary;

	private boolean isSimplified;
}
