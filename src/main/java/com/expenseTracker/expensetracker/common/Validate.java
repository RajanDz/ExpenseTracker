package com.expenseTracker.expensetracker.common;


import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    public static BigDecimal positive(BigDecimal value, String filed){
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException(filed + " must be positive");
        }
        return value;
    }
}
