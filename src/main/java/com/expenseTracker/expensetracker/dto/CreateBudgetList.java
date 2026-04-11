package com.expenseTracker.expensetracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CreateBudgetList {

    @NotNull
    private String name;
    @NotNull
    private BigDecimal budget;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
}
