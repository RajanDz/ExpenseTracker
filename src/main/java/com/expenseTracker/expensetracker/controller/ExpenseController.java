package com.expenseTracker.expensetracker.controller;


import com.expenseTracker.expensetracker.dto.*;
import com.expenseTracker.expensetracker.model.BudgetList;
import com.expenseTracker.expensetracker.model.Expense;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.service.ExpenseService;
import com.expenseTracker.expensetracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;


    @PostMapping
    public ResponseEntity<BudgetResponseDto> createAndAddExpense(@Valid @RequestBody CreateExpenseDto createExpenseDto, Authentication authentication){
        User user = userService.getLoggedUser(authentication);
        BudgetList budgetList = expenseService.addExpenseToBudgetList(createExpenseDto,user);
        return ResponseEntity.ok(new BudgetResponseDto(budgetList.getId(),budgetList.getName(),budgetList.getRemainingBudget(),budgetList.getBudget(),budgetList.getStartDate(),budgetList.getEndDate(),String.valueOf(budgetList.getType())));
    }

    @PatchMapping
    public ResponseEntity<ReturnExpenseDto> updateExpenseFields(@Valid @RequestBody UpdateExpenseFields updateExpenseFields, Authentication authentication){
        User user = userService.getLoggedUser(authentication);
        Expense updateExpense = expenseService.updateExpenseFields(updateExpenseFields, user);
        return ResponseEntity.ok(new ReturnExpenseDto(updateExpense.getName(),updateExpense.getCategory()));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(name = "expenseId") long expenseId,Authentication authentication){
        User user = userService.getLoggedUser(authentication);
        expenseService.deleteExpense(expenseId,user);
        return ResponseEntity.noContent().build();
    }


}
