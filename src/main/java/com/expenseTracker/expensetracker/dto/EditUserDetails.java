package com.expenseTracker.expensetracker.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
public class EditUserDetails {
    @Size(min = 3, max = 10)
    private String name;
    @Size(min = 3)
    private String lastname;
    @Size(min = 5)
    private String username;
}
