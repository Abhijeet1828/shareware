package com.custom.sharewise.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ExpenseSummaryDto implements Serializable, Comparable<ExpenseSummaryDto> {

	private static final long serialVersionUID = 7369159098621732094L;

	private String expenseSummaryId;

	private String title;

	private UserDto paidBy;

	private List<UserDto> paidTo;

	private BigDecimal amount;

	private String category;

	private String transactionType;

	private Date createdTimestamp;

	private Date modifiedTimestamp;

	@Override
	public int compareTo(ExpenseSummaryDto o) {
		return getCreatedTimestamp().compareTo(o.getCreatedTimestamp());
	}

}
