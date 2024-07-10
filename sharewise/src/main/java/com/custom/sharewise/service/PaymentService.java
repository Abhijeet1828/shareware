package com.custom.sharewise.service;

import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.response.GroupPaymentSummaryResponse;
import com.custom.sharewise.response.SimplifiedDebtResponse;

public interface PaymentService {

	SimplifiedDebtResponse simplifyPayments(Long groupId, CustomUserDetails userDetails);

	GroupPaymentSummaryResponse paymentSummary(Long groupId, CustomUserDetails userDetails);

}
