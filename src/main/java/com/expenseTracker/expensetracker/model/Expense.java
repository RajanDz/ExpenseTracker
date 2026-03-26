package com.expenseTracker.expensetracker.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "expense")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NonNull
    private String name;

    @Column(name = "amount")
    @NonNull
    private double amount;

    @Column(name = "date_time")
    @NonNull
    private LocalDateTime dateTime;

    @Column(name = "category")
    @NonNull
    private String category;

    @ManyToOne
    @JoinColumn(name = "budget_list")
    @Setter
    private BudgetList budgetList;



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
