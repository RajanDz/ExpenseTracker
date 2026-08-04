package com.expenseTracker.expensetracker.dto;

import java.time.LocalDate;

public record ExpenseSearchFilters(String amountSort, Long budgetId, String category,
                                   LocalDate fromDate,LocalDate toDate) {
}
