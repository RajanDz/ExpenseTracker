package com.expenseTracker.expensetracker.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "expense")
@Getter
@NoArgsConstructor

public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private  String name;

    @Column(name = "amount")
    private  double amount;

    @Column(name = "date_time")
    private   LocalDateTime dateTime;

    @Column(name = "category")
    private   String category;

    @ManyToOne
    @JoinColumn(name = "budget_list")
    private   BudgetList budgetList;

    public Expense(String name, double amount, LocalDateTime dateTime, String category, BudgetList budgetList) {
        this.name = name;
        this.amount = amount;
        this.dateTime = dateTime;
        this.category = category;
        this.budgetList = budgetList;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                ", category='" + category + '\'' +
                '}';
    }
}
