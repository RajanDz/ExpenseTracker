package com.expenseTracker.expensetracker.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class BudgetExpenseDto {

    private String name;
    private List<String> expenseName;


}
