package com.expenseTracker.expensetracker.dto;


import com.expenseTracker.expensetracker.model.Expense;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BudgetDetailsResponse {

   private List<ReturnExpenseDto> expenses;
}
