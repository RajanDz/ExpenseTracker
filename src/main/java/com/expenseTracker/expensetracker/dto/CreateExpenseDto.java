package com.expenseTracker.expensetracker.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
