package com.custom.sharewise.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
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
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { CommonException.class,
		UnauthorizedException.class }, transactionManager = "transactionManager")
public class ExpensesServiceImpl implements ExpensesService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExpensesServiceImpl.class);

	private final GroupExpensesRepository groupExpensesRepository;
	private final GroupTransactionsService groupTransactionsService;
	private final BusinessValidationService businessValidationService;
	private final ModelMapper modelMapper;

	@Override
	public Object addExpense(AddOrUpdateExpenseRequest addExpenseRequest) throws CommonException {
		try {
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
		} catch (Exception e) {
			LOGGER.error("Exception in addExpense", e);
			if (e instanceof CommonException ce)
				throw ce;
			else
				throw new CommonException(FailureConstants.ADD_GROUP_EXPENSE_ERROR.getFailureCode(),
						FailureConstants.ADD_GROUP_EXPENSE_ERROR.getFailureMsg());
		}

	}

	@Override
	public Object updateExpense(AddOrUpdateExpenseRequest updateExpenseRequest) throws CommonException {
		try {
			Map<String, Object> validations = new HashMap<>();
			validations.put(Constants.VALIDATION_GROUP_ID, updateExpenseRequest.getGroupId());
			validations.put(Constants.VALIDATION_USER_GROUP,
					new UserGroupDto(updateExpenseRequest.getGroupId(), null, updateExpenseRequest.getSplitBetween()));

			businessValidationService.validate(validations);

			GroupExpenses existingGroupExpense = groupExpensesRepository
					.findFirstByGroupExpensesIdAndIsDeletedFalse(updateExpenseRequest.getGroupExpensesId())
					.orElseThrow(() -> new CommonException(FailureConstants.GROUP_EXPENSE_NOT_FOUND.getFailureCode(),
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

			groupTransactionsService.addGroupTransaction(updateExpenseRequest,
					existingGroupExpense.getGroupExpensesId());

			return existingGroupExpense;
		} catch (Exception e) {
			LOGGER.error("Exception in updateExpense", e);
			if (e instanceof CommonException ce)
				throw ce;
			else
				throw new CommonException(FailureConstants.UPDATE_GROUP_EXPENSE_ERROR.getFailureCode(),
						FailureConstants.UPDATE_GROUP_EXPENSE_ERROR.getFailureMsg());
		}
	}

	@Override
	public Object deleteExpense(Long groupExpensesId, CustomUserDetails userDetails) throws CommonException {
		try {
			GroupExpenses existingGroupExpense = groupExpensesRepository
					.findFirstByGroupExpensesIdAndIsDeletedFalse(groupExpensesId)
					.orElseThrow(() -> new CommonException(FailureConstants.GROUP_EXPENSE_NOT_FOUND.getFailureCode(),
							FailureConstants.GROUP_EXPENSE_NOT_FOUND.getFailureMsg()));

			businessValidationService.validate(Map.of(Constants.VALIDATION_USER_GROUP,
					new UserGroupDto(existingGroupExpense.getGroupId(), null, List.of(userDetails.getUserId()))));

			existingGroupExpense.setIsDeleted(true);
			existingGroupExpense.setModifiedTimestamp(new Date());

			groupTransactionsService.softDeleteGroupTransactions(groupExpensesId);

			return groupExpensesRepository.save(existingGroupExpense);
		} catch (Exception e) {
			LOGGER.error("Exception in deleteExpense", e);
			if (e instanceof CommonException ce)
				throw ce;
			else
				throw new CommonException(FailureConstants.DELETE_GROUP_EXPENSE_ERROR.getFailureCode(),
						FailureConstants.DELETE_GROUP_EXPENSE_ERROR.getFailureMsg());
		}
	}

	@Override
	public Object restoreExpense(Long groupExpensesId, CustomUserDetails userDetails) throws CommonException {
		try {
			GroupExpenses existingGroupExpense = groupExpensesRepository
					.findFirstByGroupExpensesIdAndIsDeletedTrue(groupExpensesId)
					.orElseThrow(() -> new CommonException(FailureConstants.GROUP_EXPENSE_NOT_FOUND.getFailureCode(),
							FailureConstants.GROUP_EXPENSE_NOT_FOUND.getFailureMsg()));

			businessValidationService.validate(Map.of(Constants.VALIDATION_USER_GROUP,
					new UserGroupDto(existingGroupExpense.getGroupId(), null, List.of(userDetails.getUserId()))));

			existingGroupExpense.setIsDeleted(false);
			existingGroupExpense.setModifiedTimestamp(new Date());

			groupTransactionsService.restoreGroupTransaction(groupExpensesId);

			return groupExpensesRepository.save(existingGroupExpense);
		} catch (Exception e) {
			LOGGER.error("Exception in restoreExpense", e);
			if (e instanceof CommonException ce)
				throw ce;
			else
				throw new CommonException(FailureConstants.RESTORE_GROUP_EXPENSE_ERROR.getFailureCode(),
						FailureConstants.RESTORE_GROUP_EXPENSE_ERROR.getFailureMsg());
		}
	}

	@Override
	public List<GroupExpenses> fetchAllGroupExpenses(Long groupId) throws CommonException {
		try {
			return groupExpensesRepository.findByGroupIdAndIsDeletedFalse(groupId);
		} catch (Exception e) {
			LOGGER.error("Exception in fetchAllGroupExpenses", e);
			throw new CommonException(FailureConstants.FETCH_GROUP_EXPENSES_ERROR.getFailureCode(),
					FailureConstants.FETCH_GROUP_EXPENSES_ERROR.getFailureMsg());
		}
	}

	@Override
	public BigDecimal fetchTotalGroupExpense(Long groupId) throws CommonException {
		try {
			return groupExpensesRepository.findTotalGroupExpense(groupId);
		} catch (Exception e) {
			LOGGER.error("Exception in fetchTotalGroupExpense");
			throw new CommonException(FailureConstants.FETCH_GROUP_EXPENSES_ERROR.getFailureCode(),
					FailureConstants.FETCH_GROUP_EXPENSES_ERROR.getFailureMsg());
		}
	}

	@Override
	public List<GroupCategoryExpenseDto> fetchTotalGroupExpenseByCategories(Long groupId) throws CommonException {
		try {
			return groupExpensesRepository.findTotalGroupExpenseByCategory(groupId);
		} catch (Exception e) {
			LOGGER.error("Exception in fetchTotalGroupExpenseByCategories", e);
			throw new CommonException(FailureConstants.FETCH_GROUP_EXPENSES_ERROR.getFailureCode(),
					FailureConstants.FETCH_GROUP_EXPENSES_ERROR.getFailureMsg());
		}
	}
}
