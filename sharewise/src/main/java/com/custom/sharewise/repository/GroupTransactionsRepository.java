package com.custom.sharewise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.sharewise.model.GroupTransactions;

@Repository
public interface GroupTransactionsRepository extends JpaRepository<GroupTransactions, Long> {

	List<GroupTransactions> findAllByGroupId(Long groupId);

}
