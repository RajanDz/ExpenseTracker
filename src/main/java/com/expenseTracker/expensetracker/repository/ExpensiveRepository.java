package com.expenseTracker.expensetracker.repository;

import com.expenseTracker.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpensiveRepository extends JpaRepository<Expense,Long> {
}
