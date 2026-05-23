package com.expenseTracker.expensetracker.service;


import com.expenseTracker.expensetracker.dto.CreateExpenseDto;
import com.expenseTracker.expensetracker.dto.ExpenseDetailsResponse;
import com.expenseTracker.expensetracker.dto.UpdateExpenseFields;
import com.expenseTracker.expensetracker.model.Budget;
import com.expenseTracker.expensetracker.model.Expense;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.repository.BudgetListRepository;
import com.expenseTracker.expensetracker.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final BudgetListRepository budgetListRepository;
    private final ExpenseRepository expenseRepository;
    private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);

    public ExpenseDetailsResponse getExpenseById(User user, Long budgetId, Long expenseId){
        Budget budget = budgetListRepository.findByIdAndUserId(budgetId,user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Budget not found"));

        return expenseRepository.findByIdAndBudgetId(expenseId,budget.getId())
                .map(expense ->
                        new ExpenseDetailsResponse(expense.getName()
                                ,expense.getAmount()
                                ,expense.getDateTime()
                                ,expense.getCategory()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
    }

    @Transactional
    public Budget addExpenseToBudgetList(CreateExpenseDto createExpense, User user){
        Budget budget = budgetListRepository.findByIdAndUserId(createExpense.getBudgetId(), user.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget not found with id: " + createExpense.getBudgetId())
        );
        Expense expense = Expense.createExpense(createExpense.getTitle(),createExpense.getAmount(),createExpense.getCategory());
        budget.assignExpense(expense);
        budgetListRepository.save(budget);
        return budget;
    }

    @Transactional
    public void deleteExpense(long expenseId, User user){
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
        Budget budget = budgetListRepository.findByIdAndUserId(expense.getBudget().getId(),user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
        budget.updateBudgetAfterExpenseDelete(expense);
        expenseRepository.delete(expense);
    }

    @Transactional
    public Expense updateExpenseFields(UpdateExpenseFields expenseFields, User user){
        if (expenseFields.getName() == null && expenseFields.getCategory() == null && expenseFields.getAmount() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No fields to update");
        }
        Budget budget = budgetListRepository.findByIdAndUserId(expenseFields.getBudgetId(),user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget not found with providede userId"));
        Expense expense = expenseRepository.findByIdAndBudgetId(expenseFields.getExpenseId(), budget.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found with id: " + expenseFields.getExpenseId()));
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


}
