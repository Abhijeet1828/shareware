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

@Table(name = "group_expenses")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupExpenses extends BaseModel implements Serializable {

	private static final long serialVersionUID = -1509621817233766213L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_expenses_id")
	private Long groupExpensesId;

	@Column(name = "group_id")
	private Long groupId;

	@Column(name = "paid_by")
	private Long paidBy;

	@Column(name = "split_between")
	private String splitBetween;

	@Column(name = "total_amount", precision = 11, scale = 2)
	private BigDecimal totalAmount;

	@Column(name = "category")
	private String category;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

}
