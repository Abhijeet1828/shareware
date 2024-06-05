package com.custom.sharewise.dto;

import java.math.BigDecimal;

public record UserPaymentSummary(UserDto user, BigDecimal amount) {

}
