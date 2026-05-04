package com.expenseTracker.expensetracker.dto;


import com.expenseTracker.expensetracker.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ExpenseResponse {

    private String name;
    private BigDecimal amount;
    private Category category;
}
