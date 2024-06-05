package com.custom.sharewise.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.sharewise.model.GroupExpenses;

@Repository
public interface GroupExpensesRepository extends JpaRepository<GroupExpenses, Long> {

	Optional<GroupExpenses> findFirstByGroupExpensesIdAndIsDeletedFalse(Long groupExpensesId);

	Optional<GroupExpenses> findFirstByGroupExpensesIdAndIsDeletedTrue(Long groupExpesesId);
	
	List<GroupExpenses> findByGroupIdAndIsDeletedFalse(Long groupId);

}
