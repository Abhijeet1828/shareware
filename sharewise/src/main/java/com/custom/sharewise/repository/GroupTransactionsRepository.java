package com.custom.sharewise.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.sharewise.model.GroupTransactions;

@Repository
public interface GroupTransactionsRepository extends JpaRepository<GroupTransactions, Long> {

	List<GroupTransactions> findAllByGroupIdAndIsDeletedFalse(Long groupId);

	Long deleteByGroupExpensesId(Long groupExpensesId);

	List<GroupTransactions> findAllByGroupExpensesIdAndIsDeletedFalse(Long groupExpensesId);

	List<GroupTransactions> findAllByGroupExpensesIdAndIsDeletedTrue(Long groupExpensesId);

	Optional<GroupTransactions> findFirstByGroupTransactionsIdAndTransactionTypeAndIsDeletedFalse(
			Long groupTransactionsId, String transactionType);

	Optional<GroupTransactions> findFirstByGroupTransactionsIdAndTransactionTypeAndIsDeletedTrue(
			Long groupTransactionsId, String transactionType);

}
