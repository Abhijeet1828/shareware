package com.custom.sharewise.dto;

import java.math.BigDecimal;

public record UserPayment(UserDto owedBy, UserDto owedTo, BigDecimal amount) {

}
