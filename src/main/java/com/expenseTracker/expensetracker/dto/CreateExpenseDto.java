package com.expenseTracker.expensetracker.dto;

import com.expenseTracker.expensetracker.model.Category;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateExpenseDto {
    @NotNull
    private Long budgetId;
    @NotNull
    private String name;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private Category category;
}
