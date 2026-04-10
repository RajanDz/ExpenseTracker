package com.expenseTracker.expensetracker.repository;

import com.expenseTracker.expensetracker.model.BudgetList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BudgetListRepository extends JpaRepository<BudgetList,Long> {

    @Query("""
        select distinct b from BudgetList b
                left join fetch b.expenses
                        where b.id = :id
        """)
    Optional<BudgetList> findBudgetById(@Param("id") Long id);
    Optional<BudgetList> findByIdAndUserId(Long id,Long userId);
}
