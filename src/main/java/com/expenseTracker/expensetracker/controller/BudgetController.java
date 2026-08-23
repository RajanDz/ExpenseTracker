package com.expenseTracker.expensetracker.controller;


import com.expenseTracker.expensetracker.dto.*;
import com.expenseTracker.expensetracker.model.Budget;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.service.BudgetService;
import com.expenseTracker.expensetracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final UserService userService;



    @PostMapping
    public ResponseEntity<BudgetResponseDto> createBudgetList(@Valid @RequestBody CreateBudgetDto budgetList, Authentication authentication){
        User user = userService.getLoggedUser(authentication);
        BudgetResponseDto createBudget = budgetService.createBudgetList(budgetList,user);
         return ResponseEntity.ok(createBudget);
    }
    @GetMapping("/budgetExpense/{budgetId}")
    public ResponseEntity<PagedExpenseResponse> getBudgetExpenses(@PathVariable(name = "budgetId") long budgetId
            , Authentication authentication
            , @PageableDefault(size = 10,sort = "dateTime", direction = Sort.Direction.DESC) Pageable pageable){
        User user = userService.getLoggedUser(authentication);
        Page<ExpenseResponse> expensesList = budgetService.budgetExpenses(budgetId,user,pageable);
        return ResponseEntity.ok(new PagedExpenseResponse(
                expensesList.getContent(),
                expensesList.getNumber(),
                expensesList.getTotalPages(),
                expensesList.getTotalElements(),
                expensesList.hasNext()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponseDto> getBudget(@PathVariable(name = "id") long id, Authentication authentication){
        User user = userService.getLoggedUser(authentication);
        Budget budget = budgetService.getBudget(id,user);
        return ResponseEntity.ok().body(new BudgetResponseDto(budget.getId(), budget.getName(), budget.getBudget(), budget.getRemainingBudget(), budget.getStartDate(), budget.getEndDate(),String.valueOf(budget.getType())));
    }

    @GetMapping("/nonActiveBudgets")
    public ResponseEntity<Page<BudgetResponseDto>>getNonActiveBudgets(Authentication authentication, @PageableDefault(size = 10) Pageable pageable){
        User user = userService.getLoggedUser(authentication);
        Page<BudgetResponseDto> budgets = budgetService.getNonActiveBudgets(user, pageable);
        return ResponseEntity.ok().body(budgets);
    }

    @PatchMapping("/activateBudget/{budgetId}")
    public ResponseEntity<Void> activateBudget(Authentication authentication, @PathVariable(name = "budgetId") Long budgetId){
        User user = userService.getLoggedUser(authentication);
        budgetService.activateBudget(user,budgetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getPrimaryBudget")
    public ResponseEntity<BudgetResponseDto> getDefaultBudget(Authentication authentication){
        User user = userService.getLoggedUser(authentication);
        Budget budget = budgetService.getDefaultBudget(user);
        return ResponseEntity.ok().body(new BudgetResponseDto(budget.getId(), budget.getName(), budget.getBudget(), budget.getRemainingBudget(), budget.getStartDate(), budget.getEndDate(),String.valueOf(budget.getType())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable(name = "id") Long id, Authentication authentication){
        User user = userService.getLoggedUser(authentication);
        budgetService.deleteBudget(id,user);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/remainingDays/{budgetId}")
    public Long remainingDaysOfBudget(Authentication authentication,@PathVariable(name = "budgetId") Long id){
        User user = userService.getLoggedUser(authentication);
        return budgetService.remainingTimeOfBudget(user,id);
    }
}
