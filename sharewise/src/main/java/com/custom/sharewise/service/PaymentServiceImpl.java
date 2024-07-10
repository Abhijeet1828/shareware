package com.custom.sharewise.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.compare.ComparableUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.dto.Pair;
import com.custom.sharewise.dto.PairMaxComparator;
import com.custom.sharewise.dto.PairMinComparator;
import com.custom.sharewise.dto.UserDebts;
import com.custom.sharewise.dto.UserDto;
import com.custom.sharewise.dto.UserGroupDto;
import com.custom.sharewise.dto.UserPayment;
import com.custom.sharewise.dto.UserPaymentSummary;
import com.custom.sharewise.model.GroupTransactions;
import com.custom.sharewise.repository.GroupTransactionsRepository;
import com.custom.sharewise.response.GroupPaymentSummaryResponse;
import com.custom.sharewise.response.SimplifiedDebtResponse;
import com.custom.sharewise.validation.BusinessValidationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED, transactionManager = "transactionManager")
public class PaymentServiceImpl implements PaymentService {

	private final GroupTransactionsRepository groupTransactionsRepository;
	private final BusinessValidationService businessValidationService;
	private final UserService userService;
	private final UserGroupMappingService userGroupMappingService;

	@Override
	public SimplifiedDebtResponse simplifyPayments(Long groupId, CustomUserDetails userDetails) {
		businessValidationService.validate(Map.of(Constants.VALIDATION_USER_GROUP,
				new UserGroupDto(groupId, null, List.of(userDetails.getUserId()))));

		List<GroupTransactions> groupTransactionsList = groupTransactionsRepository
				.findAllByGroupIdAndIsDeletedFalse(groupId);
		if (CollectionUtils.isEmpty(groupTransactionsList)) {
			log.info("No debts to simplify for the groupId {}", groupId);
			return null;
		}

		List<UserDebts> userDebtList = simplifyDebts(groupTransactionsList);

		return createSimplifiedDebtResponse(userDebtList);
	}

	@Override
	public GroupPaymentSummaryResponse paymentSummary(Long groupId, CustomUserDetails userDetails) {
		businessValidationService.validate(Map.of(Constants.VALIDATION_USER_GROUP,
				new UserGroupDto(groupId, null, List.of(userDetails.getUserId()))));

		List<GroupTransactions> groupTransactionsList = groupTransactionsRepository
				.findAllByGroupIdAndIsDeletedFalse(groupId);

		Map<Long, UserDto> groupMembers = userGroupMappingService.fetchGroupMembers(groupId);

		Map<Long, BigDecimal> userToNetAmount = createUserToNetAmountMap(groupTransactionsList, groupMembers);

		List<UserPaymentSummary> userPaymentList = new ArrayList<>();
		for (Map.Entry<Long, BigDecimal> item : userToNetAmount.entrySet()) {
			UserPaymentSummary userPaymentSummary = new UserPaymentSummary(groupMembers.get(item.getKey()),
					item.getValue());

			userPaymentList.add(userPaymentSummary);
		}

		return new GroupPaymentSummaryResponse(groupId, userPaymentList);
	}

	private SimplifiedDebtResponse createSimplifiedDebtResponse(List<UserDebts> userDebtList) {
		Set<Long> userIds = userDebtList.stream().flatMap(debt -> Stream.of(debt.owedTo(), debt.owedBy()))
				.collect(Collectors.toSet());
		Map<Long, UserDto> userMap = userService.findUsersById(userIds);

		List<UserPayment> userPaymentsSummary = new ArrayList<>();
		for (UserDebts debt : userDebtList) {
			UserPayment userPayment = new UserPayment(userMap.get(debt.owedBy()), userMap.get(debt.owedTo()),
					debt.amount());

			userPaymentsSummary.add(userPayment);
		}
		return SimplifiedDebtResponse.builder().paymentSummary(userPaymentsSummary).isSimplified(true).build();
	}

	private List<UserDebts> simplifyDebts(List<GroupTransactions> groupTransactionsList) {
		Map<Long, BigDecimal> userToNetAmount = createUserToNetAmountMap(groupTransactionsList, null);

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
		List<UserDebts> userDebtList = new ArrayList<>();

		while (!receivers.isEmpty()) {
			Pair maxPair = receivers.poll();
			Pair minPair = givers.poll();

			if (maxPair.netAmount().add(minPair.netAmount()).compareTo(BigDecimal.ZERO) < 0) {
				userDebtList.add(new UserDebts(minPair.userId(), maxPair.userId(), maxPair.netAmount()));
				Pair pair = new Pair(minPair.userId(), maxPair.netAmount().add(minPair.netAmount()));
				givers.add(pair);
			} else if (maxPair.netAmount().add(minPair.netAmount()).compareTo(BigDecimal.ZERO) > 0) {
				userDebtList.add(new UserDebts(minPair.userId(), maxPair.userId(), minPair.netAmount().abs()));
				Pair pair = new Pair(maxPair.userId(), maxPair.netAmount().add(minPair.netAmount()));
				receivers.add(pair);
			} else {
				userDebtList.add(new UserDebts(minPair.userId(), maxPair.userId(), maxPair.netAmount()));
			}
		}
		return userDebtList;
	}

	private Map<Long, BigDecimal> createUserToNetAmountMap(List<GroupTransactions> groupTransactionsList,
			Map<Long, UserDto> groupMembers) {
		Map<Long, BigDecimal> userToNetAmount = new HashMap<>();

		if (MapUtils.isNotEmpty(groupMembers)) {
			for (Long userId : groupMembers.keySet()) {
				userToNetAmount.put(userId, BigDecimal.ZERO);
			}
		}

		for (GroupTransactions transaction : groupTransactionsList) {
			userToNetAmount.put(transaction.getPaidBy(), userToNetAmount
					.getOrDefault(transaction.getPaidBy(), BigDecimal.ZERO).add(transaction.getAmount()));
			userToNetAmount.put(transaction.getPaidTo(), userToNetAmount
					.getOrDefault(transaction.getPaidTo(), BigDecimal.ZERO).subtract(transaction.getAmount()));
		}

		return userToNetAmount;
	}

}
