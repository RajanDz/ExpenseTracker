package com.expenseTracker.expensetracker.exception;

public record ErrorResponse(int status,String message,long timestamp) {
}
