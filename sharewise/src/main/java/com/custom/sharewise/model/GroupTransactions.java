package com.custom.sharewise.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table
@Entity(name = "group_transactions")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupTransactions extends BaseModel implements Serializable {

	private static final long serialVersionUID = -7535428265440407960L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_transactions_id")
	private Long groupTransactionsId;

	@Column(name = "group_id")
	private Long groupId;

	@Column(name = "group_expenses_id")
	private Long groupExpensesId;

	@Column(name = "paid_by")
	private Long paidBy;

	@Column(name = "paid_to")
	private Long paidTo;

	@Column(name = "amount", precision = 11, scale = 2)
	private BigDecimal amount;

	@Column(name = "transaction_type")
	private String transactionType;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

}
