package com.expenseTracker.expensetracker.service;

import com.expenseTracker.expensetracker.dto.*;
import com.expenseTracker.expensetracker.model.BudgetList;
import com.expenseTracker.expensetracker.model.Expense;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.repository.BudgetListRepository;
import com.expenseTracker.expensetracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {


    private final UserRepository userRepository;
    private final BudgetListRepository budgetListRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Transactional
    public BudgetResponseDto createBudgetList(CreateBudgetDto budgetList, User user){
        BudgetList budget = new BudgetList(
                budgetList.getName(),
                budgetList.getBudget(),
                budgetList.getStartDate(),
                budgetList.getEndDate(),
                budgetList.getType(),
                user
        );

        userRepository.save(user);
        budgetListRepository.save(budget);
        return new BudgetResponseDto(budget.getName(),budget.getBudget(),budget.getBudget(),budget.getStartDate(),budget.getEndDate(),String.valueOf(budget.getType()));
    }

    @Transactional
    public BudgetList getBudget(long id, User user){
        return budgetListRepository.findByIdAndUserId(id,user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget not found with id:"+ id));
    }

    @Transactional
    public BudgetList getDefaultBudget(User user){
        return budgetListRepository.findFirstByUserIdAndBudgetIsNotNull(user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Budget not found"));
    }
    @Transactional
    public BudgetDetailsResponse budgetExpenses(User user, Long id){
        BudgetList budget = budgetListRepository.findByIdAndUserId(id,user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Budget not found"));
        return new BudgetDetailsResponse(budget.getExpenses().stream().map(expense -> new ReturnExpenseDto(expense.getName(),expense.getCategory())).collect(Collectors.toList()));

    }
    @Transactional
    public void deleteBudget(Long budgetId, User user){
        BudgetList budgetList = budgetListRepository.findByIdAndUserId(budgetId,user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Budget not found"));
        budgetListRepository.delete(budgetList);
    }

    public Long remainingTimeOfBudget(Long budgetId){
        BudgetList budgetList = budgetListRepository.findById(budgetId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget not found"));
        return ChronoUnit.DAYS.between(LocalDate.now(),budgetList.getEndDate());
    }


}
