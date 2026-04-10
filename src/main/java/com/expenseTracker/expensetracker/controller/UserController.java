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


}
