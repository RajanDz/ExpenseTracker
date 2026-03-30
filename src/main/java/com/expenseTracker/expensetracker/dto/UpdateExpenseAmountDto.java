package com.expenseTracker.expensetracker.dto;

import lombok.Getter;

@Getter
public class UpdateExpenseAmountDto {

    private long budgetId;
    private long expenseId;
    private double newAmount;
}
