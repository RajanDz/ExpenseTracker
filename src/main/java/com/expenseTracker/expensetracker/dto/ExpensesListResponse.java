package com.expenseTracker.expensetracker.dto;


import com.expenseTracker.expensetracker.model.Expense;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ExpensesListResponse {

    List<Expense> expenseList;
}
