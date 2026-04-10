package com.expenseTracker.expensetracker.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @NotNull
    private String name;
    @NotNull
    private String lastname;
    @NotNull
    private String username;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;

}

