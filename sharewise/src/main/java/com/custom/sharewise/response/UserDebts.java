package com.custom.sharewise.response;

import java.math.BigDecimal;

public record UserDebts(Long owedBy, Long owedTo, BigDecimal amount) {

}
