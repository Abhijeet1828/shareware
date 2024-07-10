package com.custom.sharewise.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.exception.ResourceNotFoundException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.dto.GroupCategoryExpenseDto;
import com.custom.sharewise.dto.UserGroupDto;
import com.custom.sharewise.model.GroupExpenses;
import com.custom.sharewise.repository.GroupExpensesRepository;
import com.custom.sharewise.request.AddOrUpdateExpenseRequest;
import com.custom.sharewise.validation.BusinessValidationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED, transactionManager = "transactionManager")
public class ExpensesServiceImpl implements ExpensesService {

	private final GroupExpensesRepository groupExpensesRepository;
	private final GroupTransactionsService groupTransactionsService;
	private final BusinessValidationService businessValidationService;
	private final ModelMapper modelMapper;

	@Override
	public GroupExpenses addExpense(AddOrUpdateExpenseRequest addExpenseRequest) {
		Map<String, Object> validations = new HashMap<>();
		validations.put(Constants.VALIDATION_GROUP_ID, addExpenseRequest.getGroupId());
		validations.put(Constants.VALIDATION_USER_GROUP,
				new UserGroupDto(addExpenseRequest.getGroupId(), null, addExpenseRequest.getSplitBetween()));

		businessValidationService.validate(validations);

		modelMapper.getConfiguration().setSkipNullEnabled(true);
		modelMapper.typeMap(AddOrUpdateExpenseRequest.class, GroupExpenses.class)
				.addMappings(mapper -> mapper.map(src -> StringUtils.join(src.getSplitBetween(), ","),
						GroupExpenses::setSplitBetween))
				.addMappings(mapper -> mapper.skip(GroupExpenses::setGroupExpensesId));

		GroupExpenses groupExpenses = modelMapper.map(addExpenseRequest, GroupExpenses.class);

		groupExpenses.setIsDeleted(false);
		groupExpenses.setCreatedTimestamp(new Date());
		groupExpenses.setModifiedTimestamp(new Date());

		groupExpenses = groupExpensesRepository.save(groupExpenses);

		groupTransactionsService.addGroupTransaction(addExpenseRequest, groupExpenses.getGroupExpensesId());

		return groupExpenses;
	}

	@Override
	public GroupExpenses updateExpense(AddOrUpdateExpenseRequest updateExpenseRequest) {
		Map<String, Object> validations = new HashMap<>();
		validations.put(Constants.VALIDATION_GROUP_ID, updateExpenseRequest.getGroupId());
		validations.put(Constants.VALIDATION_USER_GROUP,
				new UserGroupDto(updateExpenseRequest.getGroupId(), null, updateExpenseRequest.getSplitBetween()));

		businessValidationService.validate(validations);

		GroupExpenses existingGroupExpense = groupExpensesRepository
				.findFirstByGroupExpensesIdAndIsDeletedFalse(updateExpenseRequest.getGroupExpensesId()).orElseThrow(
						() -> new ResourceNotFoundException(FailureConstants.GROUP_EXPENSE_NOT_FOUND.getFailureCode(),
								FailureConstants.GROUP_EXPENSE_NOT_FOUND.getFailureMsg()));

		modelMapper.getConfiguration().setSkipNullEnabled(true);
		modelMapper.typeMap(AddOrUpdateExpenseRequest.class, GroupExpenses.class)
				.addMappings(mapper -> mapper.map(src -> StringUtils.join(src.getSplitBetween(), ","),
						GroupExpenses::setSplitBetween))
				.addMappings(mapper -> mapper.skip(GroupExpenses::setGroupExpensesId));

		modelMapper.map(updateExpenseRequest, existingGroupExpense);
		existingGroupExpense.setModifiedTimestamp(new Date());

		existingGroupExpense = groupExpensesRepository.save(existingGroupExpense);

		groupTransactionsService.removeGroupTransactions(existingGroupExpense.getGroupExpensesId());

		groupTransactionsService.addGroupTransaction(updateExpenseRequest, existingGroupExpense.getGroupExpensesId());

		return existingGroupExpense;
	}

	@Override
	public GroupExpenses deleteExpense(Long groupExpensesId, CustomUserDetails userDetails) {
		GroupExpenses existingGroupExpense = groupExpensesRepository
				.findFirstByGroupExpensesIdAndIsDeletedFalse(groupExpensesId).orElseThrow(
						() -> new ResourceNotFoundException(FailureConstants.GROUP_EXPENSE_NOT_FOUND.getFailureCode(),
								FailureConstants.GROUP_EXPENSE_NOT_FOUND.getFailureMsg()));

		businessValidationService.validate(Map.of(Constants.VALIDATION_USER_GROUP,
				new UserGroupDto(existingGroupExpense.getGroupId(), null, List.of(userDetails.getUserId()))));

		existingGroupExpense.setIsDeleted(true);
		existingGroupExpense.setModifiedTimestamp(new Date());

		groupTransactionsService.softDeleteGroupTransactions(groupExpensesId);

		return groupExpensesRepository.save(existingGroupExpense);
	}

	@Override
	public GroupExpenses restoreExpense(Long groupExpensesId, CustomUserDetails userDetails) {
		GroupExpenses existingGroupExpense = groupExpensesRepository
				.findFirstByGroupExpensesIdAndIsDeletedTrue(groupExpensesId).orElseThrow(
						() -> new ResourceNotFoundException(FailureConstants.GROUP_EXPENSE_NOT_FOUND.getFailureCode(),
								FailureConstants.GROUP_EXPENSE_NOT_FOUND.getFailureMsg()));

		businessValidationService.validate(Map.of(Constants.VALIDATION_USER_GROUP,
				new UserGroupDto(existingGroupExpense.getGroupId(), null, List.of(userDetails.getUserId()))));

		existingGroupExpense.setIsDeleted(false);
		existingGroupExpense.setModifiedTimestamp(new Date());

		groupTransactionsService.restoreGroupTransaction(groupExpensesId);

		return groupExpensesRepository.save(existingGroupExpense);
	}

	@Override
	public List<GroupExpenses> fetchAllGroupExpenses(Long groupId) {
		return groupExpensesRepository.findByGroupIdAndIsDeletedFalse(groupId);
	}

	@Override
	public BigDecimal fetchTotalGroupExpense(Long groupId) {
		return groupExpensesRepository.findTotalGroupExpense(groupId);
	}

	@Override
	public List<GroupCategoryExpenseDto> fetchTotalGroupExpenseByCategories(Long groupId) {
		return groupExpensesRepository.findTotalGroupExpenseByCategory(groupId);
	}
}
