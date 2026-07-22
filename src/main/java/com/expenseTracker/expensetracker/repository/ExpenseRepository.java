package com.expenseTracker.expensetracker.repository;

import com.expenseTracker.expensetracker.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense,Long>, JpaSpecificationExecutor<Expense> {
    Optional<Expense> findByIdAndBudgetId(long expenseId, long budgetId);

    Page<Expense> findByBudgetId(long budgetId, Pageable pageable);

    List<Expense> findByBudgetId(long budgetId, Specification<Expense> expenseList);
}
