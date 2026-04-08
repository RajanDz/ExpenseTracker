package com.expenseTracker.expensetracker.service;

import com.expenseTracker.expensetracker.dto.CreateBudgetList;
import com.expenseTracker.expensetracker.model.BudgetList;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.repository.BudgetListRepository;
import com.expenseTracker.expensetracker.repository.ExpenseRepository;
import com.expenseTracker.expensetracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BudgetService {


    private final UserRepository userRepository;
    private final BudgetListRepository budgetListRepository;
    private final ExpenseRepository expenseRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Transactional
    public CreateBudgetList createBudgetList(CreateBudgetList budgetList, User user){

        if (budgetList.getStartDate().isBefore(LocalDate.now()) || budgetList.getEndDate().isBefore(budgetList.getStartDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You need to provide valid date for creating list. Start date need to be after today date and start date need to be before end date.");
        }

        BudgetList budget = BudgetList.builder()
                .name(budgetList.getName())
                .budget(budgetList.getBudget())
                .remaining_budget(budgetList.getBudget())
                .startDate(budgetList.getStartDate())
                .endDate(budgetList.getEndDate())
                .build();

        user.createBudget(budget);
        userRepository.save(user);
        return new CreateBudgetList(budget.getName(),budget.getBudget(),budget.getStartDate(),budget.getEndDate());
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
