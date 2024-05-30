package com.custom.sharewise.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.apache.commons.lang3.compare.ComparableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
import com.custom.sharewise.dto.Pair;
import com.custom.sharewise.dto.PairMaxComparator;
import com.custom.sharewise.dto.PairMinComparator;
import com.custom.sharewise.model.GroupTransactions;
import com.custom.sharewise.repository.GroupTransactionsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { CommonException.class,
		UnauthorizedException.class }, transactionManager = "transactionManager")
public class SettleServiceImpl implements SettleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SettleServiceImpl.class);

	private final GroupTransactionsRepository groupTransactionsRepository;

	@Override
	public Object settleSimplify(Long groupId) throws CommonException {
		try {
			List<GroupTransactions> groupTransactionsList = groupTransactionsRepository.findAllByGroupId(groupId);
			simplifyDebts(groupTransactionsList);
		} catch (Exception e) {
			LOGGER.error("Exception in settleSimplify", e);
		}
		return null;
	}

	private void simplifyDebts(List<GroupTransactions> groupTransactionsList) {
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

		minimumTransactions(receivers, givers);
	}

	private void minimumTransactions(PriorityQueue<Pair> receivers, PriorityQueue<Pair> givers) {
		int numOfTransactions = 0;
		StringBuilder stringBuilder = new StringBuilder();

		while (!receivers.isEmpty()) {
			Pair maxPair = receivers.poll();
			Pair minPair = givers.poll();

			if (maxPair.netAmount().add(minPair.netAmount()).compareTo(BigDecimal.ZERO) < 0) {
				stringBuilder.append(minPair.userId() + "->" + maxPair.userId() + ":" + maxPair.netAmount() + "\n");
				Pair pair = new Pair(minPair.userId(), maxPair.netAmount().add(minPair.netAmount()));
				givers.add(pair);
			} else if (maxPair.netAmount().add(minPair.netAmount()).compareTo(BigDecimal.ZERO) > 0) {
				stringBuilder
						.append(minPair.userId() + "->" + maxPair.userId() + ":" + minPair.netAmount().abs() + "\n");
				Pair pair = new Pair(maxPair.userId(), maxPair.netAmount().add(minPair.netAmount()));
				receivers.add(pair);
			} else {
				stringBuilder.append(minPair.userId() + "->" + maxPair.userId() + ":" + maxPair.netAmount() + "\n");
			}
			numOfTransactions++;
		}

		LOGGER.info(stringBuilder.toString());
		LOGGER.info("Num of transactions {}", numOfTransactions);
	}

}
