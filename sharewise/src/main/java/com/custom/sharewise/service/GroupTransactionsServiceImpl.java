package com.custom.sharewise.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.model.GroupTransactions;
import com.custom.sharewise.repository.GroupTransactionsRepository;
import com.custom.sharewise.request.AddExpenseRequest;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { CommonException.class,
		UnauthorizedException.class }, transactionManager = "transactionManager")
public class GroupTransactionsServiceImpl implements GroupTransactionsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupTransactionsServiceImpl.class);

	private final GroupTransactionsRepository groupTransactionsRepository;

	public GroupTransactionsServiceImpl(GroupTransactionsRepository groupTransactionsRepository) {
		this.groupTransactionsRepository = groupTransactionsRepository;
	}

	@Override
	public void addGroupTransaction(AddExpenseRequest addExpenseRequest, Long groupExpensesId) throws CommonException {
		try {
			BigDecimal perPersonShare = addExpenseRequest.getTotalAmount()
					.divide(new BigDecimal(addExpenseRequest.getSplitBetween().size()), 2, RoundingMode.UP);

			List<GroupTransactions> groupTransactionList = new ArrayList<>();

			for (Long userId : addExpenseRequest.getSplitBetween()) {
				// Not Adding user who paid for the transaction
				if (userId.equals(addExpenseRequest.getPaidBy())) {
					continue;
				}
				GroupTransactions groupTransactions = GroupTransactions.builder()
						.groupId(addExpenseRequest.getGroupId()).groupExpensesId(groupExpensesId)
						.paidBy(addExpenseRequest.getPaidBy()).paidTo(userId).amount(perPersonShare)
						.transactionType(Constants.TRANSACTION_TYPE_GROUP_EXPENSE).isDeleted(false).build();
				groupTransactions.setCreatedTimestamp(new Date());
				groupTransactions.setModifiedTimestamp(new Date());

				groupTransactionList.add(groupTransactions);
			}

			groupTransactionsRepository.saveAll(groupTransactionList);
		} catch (Exception e) {
			LOGGER.error("Exception in addGroupTransaction", e);
			throw new CommonException(FailureConstants.ADD_GROUP_TRANSACTION_ERROR.getFailureCode(),
					FailureConstants.ADD_GROUP_TRANSACTION_ERROR.getFailureMsg());
		}
	}

}
