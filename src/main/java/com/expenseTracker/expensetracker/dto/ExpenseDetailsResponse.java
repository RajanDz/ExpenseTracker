package com.expenseTracker.expensetracker.dto;


import com.expenseTracker.expensetracker.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ExpenseDetailsResponse {
    private Long id;
    private String name;
    private BigDecimal amount;
    private LocalDateTime dateTime;
    private Category category;
}
