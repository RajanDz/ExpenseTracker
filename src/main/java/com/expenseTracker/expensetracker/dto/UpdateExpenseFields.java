package com.expenseTracker.expensetracker.dto;


import com.expenseTracker.expensetracker.model.CustomUserDetails;
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
    private CustomUserDetails.Category category;
    private BigDecimal amount;
}
