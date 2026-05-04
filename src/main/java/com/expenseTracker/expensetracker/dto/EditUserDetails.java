package com.expenseTracker.expensetracker.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class EditUserDetails {

    @Size(min = 5)
    private String username;

    @Email
    private String email;

    @Size(min = 5)
    private String password;
}
