package com.expenseTracker.expensetracker.controller;


import com.expenseTracker.expensetracker.dto.*;
import com.expenseTracker.expensetracker.model.Budget;
import com.expenseTracker.expensetracker.model.Expense;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.service.ExpenseService;
import com.expenseTracker.expensetracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/{expenseId}/{budgetId}")
    public ResponseEntity<ExpenseDetailsResponse> getExpenseById(Authentication authentication, @PathVariable(name = "expenseId") long expenseId,@PathVariable(name = "budgetId") long budgetId ){
            User user = userService.getLoggedUser(authentication);
            ExpenseDetailsResponse expense = expenseService.getExpenseById(user,budgetId,expenseId);
            return ResponseEntity.ok(expense);
    }

    @PostMapping
    public ResponseEntity<BudgetResponseDto> createAndAddExpense(@Valid @RequestBody CreateExpenseDto createExpenseDto, Authentication authentication){
        User user = userService.getLoggedUser(authentication);
        Budget budget = expenseService.addExpenseToBudgetList(createExpenseDto,user);
        return ResponseEntity.ok(new BudgetResponseDto(budget.getId(), budget.getName(), budget.getRemainingBudget(), budget.getBudget(), budget.getStartDate(), budget.getEndDate(),String.valueOf(budget.getType())));
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

    @GetMapping("/getCategories")
    public ResponseEntity<List<String>> getCategories(){
        List<String> categories = expenseService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    @PostMapping("/searchByFilters")
    public ResponseEntity<List<ExpenseResponse>> getResultByFilter(Authentication authentication,@RequestBody  ExpenseSearchFilters expenseSearchFilters, @PageableDefault(size = 10)Pageable pageable){
        User user = userService.getLoggedUser(authentication);
        List<ExpenseResponse> results = expenseService.getExpenseListByFilters(user,expenseSearchFilters,pageable);
        return ResponseEntity.ok(results);
    }
}
