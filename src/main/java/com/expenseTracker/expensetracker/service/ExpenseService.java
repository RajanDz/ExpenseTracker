package com.expenseTracker.expensetracker.service;


import com.expenseTracker.expensetracker.dto.CreateExpenseDto;
import com.expenseTracker.expensetracker.dto.ExpenseResponse;
import com.expenseTracker.expensetracker.dto.ExpensesListResponse;
import com.expenseTracker.expensetracker.dto.UpdateExpenseFields;
import com.expenseTracker.expensetracker.model.BudgetList;
import com.expenseTracker.expensetracker.model.Expense;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.repository.BudgetListRepository;
import com.expenseTracker.expensetracker.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final BudgetListRepository budgetListRepository;
    private final ExpenseRepository expenseRepository;
    private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);



    @Transactional
    public BudgetList addExpenseToBudgetList(CreateExpenseDto createExpense, User user){
        BudgetList budgetList = budgetListRepository.findByIdAndUserId(createExpense.getBudgetId(), user.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget not found with id: " + user.getId())
        );
        Expense expense = Expense.builder().name(createExpense
                        .getName())
                .amount(createExpense.getAmount())
                .dateTime(LocalDateTime.now())
                .category(createExpense.getCategory())
                .build();

        budgetList.addExpense(expense);
        budgetListRepository.save(budgetList);
        return budgetList;
    }

    @Transactional
    public void deleteExpense(long expenseId, User user){
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
        BudgetList budgetList = budgetListRepository.findByIdAndUserId(expense.getBudgetList().getId(),user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
        budgetList.updateBudgetAfterExpenseDelete(expense);
        expenseRepository.delete(expense);
    }

    @Transactional
    public Expense updateExpenseFields(UpdateExpenseFields expenseFields, User user){
        if (expenseFields.getName() == null && expenseFields.getCategory() == null && expenseFields.getAmount() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No fields to update");
        }
        BudgetList budget = budgetListRepository.findByIdAndUserId(expenseFields.getBudgetId(),user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget not found with providede userId"));
        Expense expense = expenseRepository.findByIdAndBudgetListId(expenseFields.getExpenseId(), budget.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found with id: " + expenseFields.getExpenseId()));
            if (expenseFields.getName() != null){
                expense.editName(expenseFields.getName());
            }
            if (expenseFields.getCategory() != null){
                expense.editCategory(expenseFields.getCategory());
            }
            if (expenseFields.getAmount() != null){
                budget.updateExpenseAmount(expense,expenseFields.getAmount());
            }
        expenseRepository.save(expense);
        return expense;
    }

    public Page<ExpenseResponse> budgetExpenses(long budgetId, User user, Pageable pageable){
        BudgetList budget = budgetListRepository.findByIdAndUserId(budgetId,user.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget not found"));
        return expenseRepository.findByBudgetListId(budget.getId(),pageable).map(expense -> new ExpenseResponse(expense.getName(),expense.getAmount(),expense.getCategory()));
    }
}
