package com.expenseTracker.expensetracker.service;


import com.expenseTracker.expensetracker.dto.Category;
import com.expenseTracker.expensetracker.dto.CreateBudgetList;
import com.expenseTracker.expensetracker.dto.CreateExpenseDto;
import com.expenseTracker.expensetracker.dto.UpdateExpenseAmountDto;
import com.expenseTracker.expensetracker.model.BudgetList;
import com.expenseTracker.expensetracker.model.CustomUserDetails;
import com.expenseTracker.expensetracker.model.Expense;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.repository.BudgetListRepository;
import com.expenseTracker.expensetracker.repository.ExpenseRepository;
import com.expenseTracker.expensetracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserService {

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
    public BudgetList addExpenseToBudgetList(CreateExpenseDto createExpense){
        BudgetList budgetList = budgetListRepository.findBudgetById(createExpense.getBudgetId()).orElseThrow();
        Expense expense = Expense.builder().name(createExpense
                .getName())
                .amount(createExpense.getAmount())
                .dateTime(LocalDateTime.now())
                .category(String.valueOf(Category.valueOf(createExpense.getCategory())))
                .build();

        budgetList.addExpense(expense);
        if (budgetList.getRemaining_budget() <= 0){
            logger.warn("Budget overspent for budgetId={}", budgetList.getId());
        }

        budgetListRepository.save(budgetList);
        return budgetList;
    }

    @Transactional
    public BudgetList updateExpenseAmountAndBudget(UpdateExpenseAmountDto updateAmount){
        BudgetList budget = budgetListRepository.findBudgetById(updateAmount.getBudgetId()).orElseThrow();
        Expense expense = expenseRepository.findByIdAndBudgetListId(updateAmount.getExpenseId(),budget.getId()).orElseThrow();
        budget.updateExpenseAmount(expense,updateAmount.getNewAmount());
        budgetListRepository.save(budget);
        return  budget;
    }

    public User getLoggedUser(Authentication authentication){
        CustomUserDetails authUser = (CustomUserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(authUser.getUsername()).orElseThrow();
    }

    public Long remainingTimeOfBudget(Long budgetId){
        BudgetList budgetList = budgetListRepository.findById(budgetId).orElseThrow();
        return ChronoUnit.DAYS.between(LocalDate.now(),budgetList.getEndDate());
    }
}
