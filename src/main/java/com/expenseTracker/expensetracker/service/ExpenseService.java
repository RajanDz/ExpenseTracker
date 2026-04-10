package com.expenseTracker.expensetracker.service;


import com.expenseTracker.expensetracker.dto.Category;
import com.expenseTracker.expensetracker.dto.CreateExpenseDto;
import com.expenseTracker.expensetracker.dto.UpdateExpenseAmountDto;
import com.expenseTracker.expensetracker.dto.UpdateExpenseFields;
import com.expenseTracker.expensetracker.model.BudgetList;
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
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final UserRepository userRepository;
    private final BudgetListRepository budgetListRepository;
    private final ExpenseRepository expenseRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);



    @Transactional
    public BudgetList addExpenseToBudgetList(CreateExpenseDto createExpense, User user){
//        BudgetList budgetList = budgetListRepository.findBudgetById(createExpense.getBudgetId()).orElseThrow();
        BudgetList budgetList = budgetListRepository.findByIdAndUserId(createExpense.getBudgetId(), user.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget not found with id: " + user.getId())
        );
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
    public Expense updateExpenseFields(UpdateExpenseFields expenseFields, User user){
        if (expenseFields.getName() == null && expenseFields.getCategory() == null && expenseFields.getAmount() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No fields to update");
        }
        BudgetList budget = budgetListRepository.findByIdAndUserId(expenseFields.getBudgetId(),user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget not found with providede userId"));
        Expense expense = expenseRepository.findByIdAndBudgetListId(expenseFields.getExpenseId(), budget.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found with id: " + expenseFields.getExpenseId()));

        if (expenseFields.getName() != null){
            expense.setName(expenseFields.getName());
        }
        if (expenseFields.getCategory() != null){
            expense.setCategory(String.valueOf(Category.valueOf(expenseFields.getCategory().toUpperCase())));
        }
        if (expenseFields.getAmount() > 0){
            budget.updateExpenseAmount(expense,expenseFields.getAmount());
        }
        expenseRepository.save(expense);
        return expense;
    }


}
