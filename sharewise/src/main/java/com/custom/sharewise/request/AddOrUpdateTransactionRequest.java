package com.custom.sharewise.request;

import java.io.Serializable;
import java.math.BigDecimal;

import com.custom.sharewise.validation.OnCreate;
import com.custom.sharewise.validation.OnUpdate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddOrUpdateTransactionRequest implements Serializable {

	private static final long serialVersionUID = 2731502873586366013L;

	@NotNull(groups = OnUpdate.class)
	@Positive(groups = OnUpdate.class)
	private Long groupTransactionsId;

	@NotNull(groups = { OnCreate.class, OnUpdate.class })
	@Positive(groups = { OnCreate.class, OnUpdate.class })
	private Long groupId;

	@NotNull(groups = { OnCreate.class, OnUpdate.class })
	@Positive(groups = { OnCreate.class, OnUpdate.class })
	private Long paidBy;

	@NotNull(groups = { OnCreate.class, OnUpdate.class })
	@Positive(groups = { OnCreate.class, OnUpdate.class })
	private Long paidTo;

	@NotNull(groups = { OnCreate.class, OnUpdate.class })
	@Positive(groups = { OnCreate.class, OnUpdate.class })
	private BigDecimal amount;

}
