package com.custom.sharewise.service;

import java.util.Date;
import java.util.HashMap;
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
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.dto.UserGroupDto;
import com.custom.sharewise.model.GroupExpenses;
import com.custom.sharewise.repository.GroupExpensesRepository;
import com.custom.sharewise.request.AddExpenseRequest;
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
	public Object addExpense(AddExpenseRequest addExpenseRequest) throws CommonException {
		try {
			Map<String, Object> validations = new HashMap<>();
			validations.put(Constants.VALIDATION_GROUP_ID, addExpenseRequest.getGroupId());
			validations.put(Constants.VALIDATION_USER_GROUP,
					new UserGroupDto(addExpenseRequest.getGroupId(), null, addExpenseRequest.getSplitBetween()));

			businessValidationService.validate(validations);

			modelMapper.getConfiguration().setSkipNullEnabled(true);
			modelMapper.typeMap(AddExpenseRequest.class, GroupExpenses.class).addMappings(mapper -> mapper
					.map(src -> StringUtils.join(src.getSplitBetween(), ","), GroupExpenses::setSplitBetween));

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

}
