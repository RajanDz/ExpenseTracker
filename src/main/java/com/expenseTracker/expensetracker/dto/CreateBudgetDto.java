package com.expenseTracker.expensetracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class CreateBudgetDto {

    @NotNull
    private String name;
    @NotNull
    private BigDecimal budget;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
}
