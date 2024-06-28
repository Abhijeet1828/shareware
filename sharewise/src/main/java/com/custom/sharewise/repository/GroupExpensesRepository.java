package com.custom.sharewise.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.custom.sharewise.dto.GroupCategoryExpenseDto;
import com.custom.sharewise.model.GroupExpenses;

@Repository
public interface GroupExpensesRepository extends JpaRepository<GroupExpenses, Long> {

	Optional<GroupExpenses> findFirstByGroupExpensesIdAndIsDeletedFalse(Long groupExpensesId);

	Optional<GroupExpenses> findFirstByGroupExpensesIdAndIsDeletedTrue(Long groupExpesesId);

	List<GroupExpenses> findByGroupIdAndIsDeletedFalse(Long groupId);

	@Query(value = "SELECT SUM(e.totalAmount) FROM GroupExpenses e WHERE e.groupId = :groupId AND isDeleted = false")
	BigDecimal findTotalGroupExpense(@Param("groupId") Long groupId);

	@Query(value = "SELECT new com.custom.sharewise.dto.GroupCategoryExpenseDto(g.category, SUM(g.totalAmount)) "
			+ "FROM GroupExpenses g WHERE g.groupId = :groupId AND isDeleted = false GROUP BY g.category ORDER BY SUM(g.totalAmount) DESC")
	List<GroupCategoryExpenseDto> findTotalGroupExpenseByCategory(Long groupId);

}
