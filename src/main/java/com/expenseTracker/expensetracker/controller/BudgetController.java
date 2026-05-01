package com.expenseTracker.expensetracker.controller;


import com.expenseTracker.expensetracker.dto.BudgetDetailsResponse;
import com.expenseTracker.expensetracker.dto.BudgetResponseDto;
import com.expenseTracker.expensetracker.dto.CreateBudgetDto;
import com.expenseTracker.expensetracker.model.BudgetList;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.service.BudgetService;
import com.expenseTracker.expensetracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponseDto> getBudget(@PathVariable(name = "id") long id, Authentication authentication){
        User user = userService.getLoggedUser(authentication);
        BudgetList budgetList = budgetService.getBudget(id,user);
        return ResponseEntity.ok().body(new BudgetResponseDto(budgetList.getName(),budgetList.getBudget(),budgetList.getRemainingBudget(),budgetList.getStartDate(),budgetList.getEndDate(),String.valueOf(budgetList.getType())));
    }

    @GetMapping("/getPrimaryBudget")
    public ResponseEntity<BudgetResponseDto> getDefaultBudget(Authentication authentication){
        User user = userService.getLoggedUser(authentication);
        BudgetList budgetList = budgetService.getDefaultBudget(user);
        return ResponseEntity.ok().body(new BudgetResponseDto(budgetList.getName(),budgetList.getBudget(),budgetList.getRemainingBudget(),budgetList.getStartDate(),budgetList.getEndDate(),String.valueOf(budgetList.getType())));
    }
    @GetMapping("/getBudgetExpenses/{id}")
    public ResponseEntity<BudgetDetailsResponse> getBudgetExpenses(@PathVariable(name = "id") Long id, Authentication authentication){
        User user = userService.getLoggedUser(authentication);
        BudgetDetailsResponse budgetResponseDto = budgetService.budgetExpenses(user,id);
        return ResponseEntity.ok(budgetResponseDto);
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
