package com.expenseTracker.expensetracker.dto;

import com.expenseTracker.expensetracker.model.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateExpenseDto {
    @NotNull
    private Long budgetId;
    @NotNull
    private String name;
    @NotNull
    @Positive
    private BigDecimal amount;
    @NotNull
    private Category category;
}
