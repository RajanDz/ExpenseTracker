package com.expenseTracker.expensetracker.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
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
    @NotNull
    @Setter
    private String name;

    @Column(name = "amount")
    @NotNull
    @Setter
    private BigDecimal amount;

    @Column(name = "date_time", updatable = false)
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "category")
    @NotNull
    @Setter
    private String category;

    @ManyToOne
    @JoinColumn(name = "budget_list")
    @Setter
    private BudgetList budgetList;

    public void editName(String name){
        this.name = name;
    }
    public void editCategory(String category){
        this.category = category;
    }

}
