package com.expenseTracker.expensetracker.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UpdateExpenseFields {
    @NotNull
    private long budgetId;
    @NotNull
    private long expenseId;
    private String name;
    private String category;
    private BigDecimal amount;
}
