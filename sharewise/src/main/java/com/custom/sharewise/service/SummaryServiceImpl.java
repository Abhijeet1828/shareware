package com.custom.sharewise.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.convertors.CommonConversionUtils;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.dto.ExpenseSummaryDto;
import com.custom.sharewise.dto.GroupCategoryExpenseDto;
import com.custom.sharewise.dto.UserDto;
import com.custom.sharewise.dto.UserGroupDto;
import com.custom.sharewise.model.GroupExpenses;
import com.custom.sharewise.model.GroupTransactions;
import com.custom.sharewise.response.GroupExpenseSummaryResponse;
import com.custom.sharewise.response.TotalGroupExpenseSummaryResponse;
import com.custom.sharewise.validation.BusinessValidationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED, transactionManager = "transactionManager")
public class SummaryServiceImpl implements SummaryService {

	private final GroupTransactionsService groupTransactionsService;
	private final ExpensesService expensesService;
	private final UserGroupMappingService userGroupMappingService;
	private final BusinessValidationService businessValidationService;

	@Override
	public GroupExpenseSummaryResponse fetchGroupExpenseSummary(Long groupId, CustomUserDetails userDetails) {
		businessValidationService.validate(Map.of(Constants.VALIDATION_USER_GROUP,
				new UserGroupDto(groupId, null, List.of(userDetails.getUserId()))));

		List<GroupExpenses> groupExpensesList = expensesService.fetchAllGroupExpenses(groupId);
		List<GroupTransactions> userTransactionList = groupTransactionsService.fetchAllGroupTransactionsByType(groupId,
				Constants.TRANSACTION_TYPE_USER_PAYMENT);
		Map<Long, UserDto> groupMembers = userGroupMappingService.fetchGroupMembers(groupId);

		List<ExpenseSummaryDto> expenseSummaryList = new ArrayList<>();

		for (GroupExpenses expense : groupExpensesList) {
			ExpenseSummaryDto expenseSummaryDto = new ExpenseSummaryDto();
			expenseSummaryDto.setExpenseSummaryId("EXPENSE-" + expense.getGroupExpensesId());
			expenseSummaryDto.setTitle(expense.getTitle());
			expenseSummaryDto.setPaidBy(groupMembers.get(expense.getPaidBy()));

			List<UserDto> paidToList = new ArrayList<>();

			for (Long userId : CommonConversionUtils.convertFromCommaSeperatedStrings(expense.getSplitBetween())) {
				paidToList.add(groupMembers.get(userId));
			}
			expenseSummaryDto.setPaidTo(paidToList);
			expenseSummaryDto.setAmount(expense.getTotalAmount());
			expenseSummaryDto.setCategory(expense.getCategory());
			expenseSummaryDto.setTransactionType(Constants.TRANSACTION_TYPE_GROUP_EXPENSE);
			expenseSummaryDto.setCreatedTimestamp(expense.getCreatedTimestamp());
			expenseSummaryDto.setModifiedTimestamp(expense.getModifiedTimestamp());

			expenseSummaryList.add(expenseSummaryDto);
		}

		for (GroupTransactions transactions : userTransactionList) {
			ExpenseSummaryDto expenseSummaryDto = new ExpenseSummaryDto();
			expenseSummaryDto.setExpenseSummaryId("PAYMENT-" + transactions.getGroupTransactionsId());
			expenseSummaryDto.setTitle("USER-PAYMENT");
			expenseSummaryDto.setPaidBy(groupMembers.get(transactions.getPaidBy()));
			expenseSummaryDto.setPaidTo(List.of(groupMembers.get(transactions.getPaidTo())));
			expenseSummaryDto.setAmount(transactions.getAmount());
			expenseSummaryDto.setCategory("USER-PAYMENT");
			expenseSummaryDto.setTransactionType(Constants.TRANSACTION_TYPE_USER_PAYMENT);
			expenseSummaryDto.setCreatedTimestamp(transactions.getCreatedTimestamp());
			expenseSummaryDto.setModifiedTimestamp(transactions.getModifiedTimestamp());

			expenseSummaryList.add(expenseSummaryDto);
		}

		Collections.sort(expenseSummaryList);

		return new GroupExpenseSummaryResponse(groupId, expenseSummaryList);
	}

	@Override
	public TotalGroupExpenseSummaryResponse fetchTotalGroupExpense(Long groupId, CustomUserDetails userDetails) {
		businessValidationService.validate(Map.of(Constants.VALIDATION_USER_GROUP,
				new UserGroupDto(groupId, null, List.of(userDetails.getUserId()))));

		BigDecimal totalGroupExpenditure = expensesService.fetchTotalGroupExpense(groupId);

		List<GroupCategoryExpenseDto> groupCategoryExpenses = expensesService
				.fetchTotalGroupExpenseByCategories(groupId);

		List<GroupExpenses> groupExpensesList = expensesService.fetchAllGroupExpenses(groupId);

		Map<Long, BigDecimal> userExpenditureMap = new HashMap<>();
		for (GroupExpenses expenses : groupExpensesList) {
			List<Long> userIds = CommonConversionUtils.convertFromCommaSeperatedStrings(expenses.getSplitBetween());
			BigDecimal perPersonShare = expenses.getTotalAmount().divide(new BigDecimal(userIds.size()), 2,
					RoundingMode.UP);
			for (Long userId : userIds) {
				userExpenditureMap.put(userId,
						userExpenditureMap.getOrDefault(userId, BigDecimal.ZERO).add(perPersonShare));
			}
		}

		Map<Long, UserDto> groupMembers = userGroupMappingService.fetchGroupMembers(groupId);

		Map<String, BigDecimal> userExpenseMap = new HashMap<>();
		for (Map.Entry<Long, UserDto> member : groupMembers.entrySet()) {
			userExpenseMap.put(member.getValue().name(),
					userExpenditureMap.getOrDefault(member.getKey(), BigDecimal.ZERO));
		}

		return new TotalGroupExpenseSummaryResponse(totalGroupExpenditure, groupCategoryExpenses, userExpenseMap);
	}

}
