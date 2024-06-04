package com.custom.sharewise.dto;

import java.math.BigDecimal;

public record UserDebts(Long owedBy, Long owedTo, BigDecimal amount) {

}
