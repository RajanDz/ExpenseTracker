package com.expenseTracker.expensetracker.controller;


import com.expenseTracker.expensetracker.dto.CreateBudgetList;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.service.BudgetService;
import com.expenseTracker.expensetracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final UserService userService;


    @PostMapping("/createBudget")
    public ResponseEntity<CreateBudgetList> createBudgetList(@Valid @RequestBody CreateBudgetList budgetList, Authentication authentication){

        User user = userService.getLoggedUser(authentication);
        CreateBudgetList createBudget = budgetService.createBudgetList(budgetList,user);
        return ResponseEntity.ok(createBudget);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable(name = "id") Long id, Authentication authentication){
        User user = userService.getLoggedUser(authentication);
        budgetService.deleteBudget(id,user);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/remainingDays/{budgetId}")
    public Long remainingDaysOfBudget(@PathVariable(name = "budgetId") Long id){
        return budgetService.remainingTimeOfBudget(id);
    }
}
