package com.expenseTracker.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReturnExpenseDto {
    private String name;
    private Category category;
}
