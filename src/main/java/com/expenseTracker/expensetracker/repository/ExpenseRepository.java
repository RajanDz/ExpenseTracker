package com.expenseTracker.expensetracker.repository;

import com.expenseTracker.expensetracker.model.BudgetList;
import com.expenseTracker.expensetracker.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    Optional<Expense> findByIdAndBudgetListId(long expenseId, long budgetId);

    Page<Expense> findByBudgetListId(long budgetId, Pageable pageable);
}
