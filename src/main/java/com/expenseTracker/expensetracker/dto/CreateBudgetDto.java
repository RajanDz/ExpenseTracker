package com.expenseTracker.expensetracker.dto;

import com.expenseTracker.expensetracker.model.BudgetTypes;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class CreateBudgetDto {

    @NotNull
    private String name;
    @NotNull
    @Positive
    private BigDecimal budget;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private BudgetTypes type;
}
