package com.expenseTracker.expensetracker.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "budget_list")
@Getter
@ToString(exclude = "expenses")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BudgetList {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private  String name;

    @Column(name = "start_date")
    private  LocalDate startDate;

    @Column(name = "end_date")
    private   LocalDate endDate;

    @OneToMany(mappedBy = "budgetList", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Expense> expenses = new ArrayList<>();

    public void addExpensive(Expense expense){
        this.expenses.add(expense);
        expense.setBudgetList(this);
    }


}
