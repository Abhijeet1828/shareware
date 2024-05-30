package com.custom.sharewise.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.custom.common.utilities.validators.SafeInput;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddExpenseRequest implements Serializable {

	private static final long serialVersionUID = -8961988988410077237L;

	@Positive
	@NotNull
	private Long groupId;

	@Positive
	@NotNull
	private Long paidBy;

	@NotEmpty
	private List<Long> splitBetween;

	@Positive
	@NotNull
	private BigDecimal totalAmount;

	@SafeInput
	private String category;

}
