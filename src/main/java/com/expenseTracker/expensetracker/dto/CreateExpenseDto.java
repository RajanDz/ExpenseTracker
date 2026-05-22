package com.expenseTracker.expensetracker.dto;

import com.expenseTracker.expensetracker.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateExpenseDto {
    @NotNull
    private Long budgetId;
    @NotBlank
    private String title;
    @NotNull
    @Positive
    private BigDecimal amount;
    @NotNull
    private Category category;
}
