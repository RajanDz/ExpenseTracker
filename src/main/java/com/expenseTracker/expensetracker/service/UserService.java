package com.expenseTracker.expensetracker.service;


import com.expenseTracker.expensetracker.dto.CreateBudgetList;
import com.expenseTracker.expensetracker.model.BudgetList;
import com.expenseTracker.expensetracker.model.CustomUserDetails;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.repository.BudgetListRepository;
import com.expenseTracker.expensetracker.repository.UserRepository;
import com.expenseTracker.expensetracker.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BudgetListRepository budgetListRepository;

    public CreateBudgetList createBudgetList(CreateBudgetList budgetList, User user){

        if (budgetList.getStartDate().isBefore(LocalDate.now()) || budgetList.getEndDate().isBefore(budgetList.getStartDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You need to provide valid date for creating list. Start date need to be after today date and start date need to be before end date.");
        }

        BudgetList budget = BudgetList.builder()
                .name(budgetList.getName())
                .startDate(budgetList.getStartDate())
                .endDate(budgetList.getEndDate())
                .build();

        user.createBudget(budget);
        userRepository.save(user);
        return new CreateBudgetList(budget.getName(),budget.getStartDate(),budget.getEndDate());
    }
    public User getLoggedUser(Authentication authentication){
        CustomUserDetails authUser = (CustomUserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(authUser.getUsername()).orElseThrow();
    }

    public Long remainingTimeOfBudget(Long budgetId){
        BudgetList budgetList = budgetListRepository.findById(budgetId).orElseThrow();
        return ChronoUnit.DAYS.between(budgetList.getEndDate(),LocalDate.now());
    }
}
