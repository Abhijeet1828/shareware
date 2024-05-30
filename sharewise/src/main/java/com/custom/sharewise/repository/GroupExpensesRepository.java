package com.custom.sharewise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.sharewise.model.GroupExpenses;

@Repository
public interface GroupExpensesRepository extends JpaRepository<GroupExpenses, Long> {

}
