package com.expenseTracker.expensetracker.common;


import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
public class Validate {

    public static String text(String value, String filed){
        if (value == null || value.isBlank()){
            throw new IllegalArgumentException(filed + " must not be blank");
        }
        return value;
    }

    public static <T> T notNull(T value, String field){
        if (value == null){
            throw new IllegalArgumentException(field + " must not be null");
        }
        return value;
    }
    public static void dateRange(LocalDate startDate, LocalDate endDate){
        if (startDate.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Start date must not be in the past");
        }
        if (endDate.isBefore(startDate)){
            throw new IllegalArgumentException("End date must be after start date");
        }
    }
    public static BigDecimal positive(BigDecimal value, String filed){
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException(filed + " must be positive");
        }
        return value;
    }
}
