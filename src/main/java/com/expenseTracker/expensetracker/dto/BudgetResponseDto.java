package com.expenseTracker.expensetracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class BudgetResponseDto {

    @NotNull
    private String name;
    @NotNull
    private BigDecimal budget;
    @NotNull
    private BigDecimal remainingBudget;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private String type;
}
