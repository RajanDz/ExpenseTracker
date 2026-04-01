package com.expenseTracker.expensetracker.controller;


import com.expenseTracker.expensetracker.dto.*;
import com.expenseTracker.expensetracker.model.BudgetList;
import com.expenseTracker.expensetracker.model.Expense;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public String getAuthUser(Authentication authentication){
        return authentication.getName();
    }

    @PostMapping("/createBudget")
    public ResponseEntity<CreateBudgetList> createBudgetList(@Valid @RequestBody CreateBudgetList budgetList, Authentication authentication){

        User user = userService.getLoggedUser(authentication);
        CreateBudgetList createBudget = userService.createBudgetList(budgetList,user);
        return ResponseEntity.ok(createBudget);
    }

    @PostMapping("/createAndAddExpense")
    public ResponseEntity<CreateBudgetList> createAndAddExpense(@Valid @RequestBody CreateExpenseDto createExpenseDto, Authentication authentication){
        User user = userService.getLoggedUser(authentication);
        BudgetList budgetList = userService.addExpenseToBudgetList(createExpenseDto,user);
        return ResponseEntity.ok(new CreateBudgetList(budgetList.getName(),budgetList.getRemaining_budget(),budgetList.getStartDate(),budgetList.getEndDate()));
    }

    @PatchMapping("/updateExpenseFields")
    public ResponseEntity<ReturnExpenseDto> updateExpenseFields(@Valid @RequestBody UpdateExpenseFields updateExpenseFields){
        Expense updateExpense = userService.updateExpenseFields(updateExpenseFields);
        return ResponseEntity.ok(new ReturnExpenseDto(updateExpense.getName(),updateExpense.getCategory()));
    }
    @PatchMapping("/updateAmount")
    public ResponseEntity<CreateBudgetList> updateAmount(@Valid @RequestBody UpdateExpenseAmountDto updateExpenseAmountDto){
        BudgetList budgetList = userService.updateExpenseAmountAndBudget(updateExpenseAmountDto);
        return  ResponseEntity.ok(new CreateBudgetList(budgetList.getName(),budgetList.getRemaining_budget(),budgetList.getStartDate(),budgetList.getEndDate()));
    }

    @GetMapping("/remainingDays/{budgetId}")
    public Long remainingDaysOfBudget(@PathVariable(name = "budgetId") Long id){
        return userService.remainingTimeOfBudget(id);
    }
}
