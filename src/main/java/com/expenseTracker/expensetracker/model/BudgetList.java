package com.expenseTracker.expensetracker.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "budget_list")
@Getter
@ToString
@NoArgsConstructor
public class BudgetList {
    public BudgetList(String name, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private  LocalDate startDate;

    @Column(name = "end_date")
    private  LocalDate endDate;

    @OneToMany(mappedBy = "budgetList", cascade = CascadeType.ALL)
    private List<Expense> expenses = new ArrayList<>();



}
