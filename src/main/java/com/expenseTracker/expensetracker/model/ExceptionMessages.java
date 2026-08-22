package com.expenseTracker.expensetracker.model;

public enum ExceptionMessages {
    BUDGET_NOT_FOUND("Budget not found");
    private final String message;

    ExceptionMessages(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
