package com.expenseTracker.expensetracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateExpenseDto {
    @NotNull
    private Long budgetId;
    @NotNull
    private String name;
    @NotNull
    private Double amount;
    @NotNull
    private String category;
}
