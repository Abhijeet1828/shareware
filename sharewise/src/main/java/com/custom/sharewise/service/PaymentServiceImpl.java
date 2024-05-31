package com.custom.sharewise.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.compare.ComparableUtils;
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
import com.custom.sharewise.dto.Pair;
import com.custom.sharewise.dto.PairMaxComparator;
import com.custom.sharewise.dto.PairMinComparator;
import com.custom.sharewise.dto.UserGroupDto;
import com.custom.sharewise.model.GroupTransactions;
import com.custom.sharewise.repository.GroupTransactionsRepository;
import com.custom.sharewise.response.PaymentSummaryResponse;
import com.custom.sharewise.response.UserDebts;
import com.custom.sharewise.validation.BusinessValidationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { CommonException.class,
		UnauthorizedException.class }, transactionManager = "transactionManager")
public class PaymentServiceImpl implements PaymentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

	private final GroupTransactionsRepository groupTransactionsRepository;
	private final BusinessValidationService businessValidationService;

	@Override
	public Object simplifyPayments(Long groupId, CustomUserDetails userDetails) throws CommonException {
		try {
			businessValidationService.validate(Map.of(Constants.VALIDATION_USER_GROUP,
					new UserGroupDto(groupId, null, List.of(userDetails.getUserId()))));

			List<GroupTransactions> groupTransactionsList = groupTransactionsRepository
					.findAllByGroupIdAndIsDeletedFalse(groupId);
			if (CollectionUtils.isEmpty(groupTransactionsList)) {
				LOGGER.info("No debts to simplify for the groupId {}", groupId);
				return 1;
			}

			List<UserDebts> userDebtList = simplifyDebts(groupTransactionsList);

			return PaymentSummaryResponse.builder().paymentSummary(userDebtList).isSimplified(true).build();
		} catch (Exception e) {
			LOGGER.error("Exception in settleSimplify", e);
			if (e instanceof CommonException ce)
				throw ce;
			else
				throw new CommonException(FailureConstants.SIMPLIFY_PAYMENTS_ERROR.getFailureCode(),
						FailureConstants.SIMPLIFY_PAYMENTS_ERROR.getFailureMsg());
		}
	}

	private List<UserDebts> simplifyDebts(List<GroupTransactions> groupTransactionsList) {
		Map<Long, BigDecimal> userToNetAmount = new HashMap<>();

		for (GroupTransactions transaction : groupTransactionsList) {
			userToNetAmount.put(transaction.getPaidBy(), userToNetAmount
					.getOrDefault(transaction.getPaidBy(), BigDecimal.ZERO).add(transaction.getAmount()));
			userToNetAmount.put(transaction.getPaidTo(), userToNetAmount
					.getOrDefault(transaction.getPaidTo(), BigDecimal.ZERO).subtract(transaction.getAmount()));
		}

		PriorityQueue<Pair> receivers = new PriorityQueue<>(new PairMaxComparator());
		PriorityQueue<Pair> givers = new PriorityQueue<>(new PairMinComparator());

		for (Map.Entry<Long, BigDecimal> item : userToNetAmount.entrySet()) {
			Pair pair = new Pair(item.getKey(), item.getValue());

			if (item.getValue().compareTo(BigDecimal.ZERO) != 0) {
				if (ComparableUtils.is(item.getValue()).lessThan(BigDecimal.ZERO)) {
					givers.add(pair);
				} else {
					receivers.add(pair);
				}
			}
		}

		return minimumTransactions(receivers, givers);
	}

	private List<UserDebts> minimumTransactions(PriorityQueue<Pair> receivers, PriorityQueue<Pair> givers) {
		// int numOfTransactions = 0;
		// StringBuilder stringBuilder = new StringBuilder();
		List<UserDebts> userDebtList = new ArrayList<>();

		while (!receivers.isEmpty()) {
			Pair maxPair = receivers.poll();
			Pair minPair = givers.poll();

			if (maxPair.netAmount().add(minPair.netAmount()).compareTo(BigDecimal.ZERO) < 0) {
				userDebtList.add(new UserDebts(minPair.userId(), maxPair.userId(), maxPair.netAmount()));
				// stringBuilder.append(minPair.userId() + "->" + maxPair.userId() + ":" +
				// maxPair.netAmount() + "\n");
				Pair pair = new Pair(minPair.userId(), maxPair.netAmount().add(minPair.netAmount()));
				givers.add(pair);
			} else if (maxPair.netAmount().add(minPair.netAmount()).compareTo(BigDecimal.ZERO) > 0) {
				userDebtList.add(new UserDebts(minPair.userId(), maxPair.userId(), minPair.netAmount().abs()));
				// stringBuilder.append(minPair.userId() + "->" + maxPair.userId() + ":" +
				// minPair.netAmount().abs() + "\n");
				Pair pair = new Pair(maxPair.userId(), maxPair.netAmount().add(minPair.netAmount()));
				receivers.add(pair);
			} else {
				userDebtList.add(new UserDebts(minPair.userId(), maxPair.userId(), maxPair.netAmount()));
				// stringBuilder.append(minPair.userId() + "->" + maxPair.userId() + ":" +
				// maxPair.netAmount() + "\n");
			}
			// numOfTransactions++;
		}
		return userDebtList;

		/*
		 * LOGGER.info(stringBuilder.toString()); LOGGER.info("Num of transactions {}",
		 * numOfTransactions);
		 */
	}

}
