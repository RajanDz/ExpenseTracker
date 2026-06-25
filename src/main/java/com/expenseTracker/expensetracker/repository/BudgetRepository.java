package com.expenseTracker.expensetracker.repository;

import com.expenseTracker.expensetracker.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget,Long> {

    @Query("""
        select distinct b from Budget b
                left join fetch b.expenses
                        where b.id = :id
        """)
    Optional<Budget> findBudgetById(@Param("id") Long id);
    Optional<Budget> findByIdAndUserId(Long id, Long userId);
    Boolean existsByIdAndUserId(Long id, Long userId);


    Optional<Budget> findByUserIdAndActiveTrue(long userId);
    Optional<Budget> findFirstByUserIdAndBudgetIsNotNull(long userId);}
