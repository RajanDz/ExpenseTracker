package com.expenseTracker.expensetracker.excpetion;

public record ErrorResponse(int status,String message,long timestamp) {
}
