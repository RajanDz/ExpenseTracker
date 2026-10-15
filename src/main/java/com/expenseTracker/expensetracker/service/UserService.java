package com.expenseTracker.expensetracker.service;


import com.expenseTracker.expensetracker.dto.*;
import com.expenseTracker.expensetracker.model.CustomUserDetails;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.repository.BudgetRepository;
import com.expenseTracker.expensetracker.repository.ExpenseRepository;
import com.expenseTracker.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User getLoggedUser(@NonNull Authentication authentication){
        CustomUserDetails authUser = (CustomUserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(authUser.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public void editUserDetail(User user, EditUserDetails userDetails){
            if (userDetails.getName() != null && !userDetails.getName().isBlank()){
                    user.editName(userDetails.getName());
            }
            if (userDetails.getLastname() != null && !userDetails.getLastname().isBlank()){
                user.editLastname(userDetails.getLastname());
            }
            if (userDetails.getUsername() != null && !userDetails.getUsername().isBlank()){
                if (userRepository.findByUsername(userDetails.getUsername()).isPresent()){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist");
                }
                user.editUsername(userDetails.getUsername());
            }

        userRepository.save(user);
    }

}
