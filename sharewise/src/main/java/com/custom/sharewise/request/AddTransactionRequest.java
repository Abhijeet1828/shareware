package com.custom.sharewise.request;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddTransactionRequest implements Serializable {

	private static final long serialVersionUID = 2731502873586366013L;

	@NotNull
	@Positive
	private Long groupId;

	@NotNull
	@Positive
	private Long paidBy;

	@NotNull
	@Positive
	private Long paidTo;

	@NotNull
	@Positive
	private BigDecimal amount;

}
