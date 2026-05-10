package com.expenseTracker.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PagedExpenseResponse {
    List<ExpenseResponse> expenseList;
    int currentPage;
    int totalPages;
    long totalElements;
    boolean hasNext;
}
