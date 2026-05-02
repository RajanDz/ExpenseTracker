package com.expenseTracker.expensetracker.dto;

import com.expenseTracker.expensetracker.model.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReturnExpenseDto {
    private String name;
    private CustomUserDetails.Category category;
}
