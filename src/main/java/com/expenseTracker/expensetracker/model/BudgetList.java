package com.expenseTracker.expensetracker.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "budget_list")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BudgetList {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private  String name;

    @Column(name = "budget")
    private BigDecimal budget;

    @Column(name = "remaining_budget")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal remaining_budget;

    @Column(name = "start_date")
    private  LocalDate startDate;

    @Column(name = "end_date")
    private   LocalDate endDate;

    @OneToMany(mappedBy = "budgetList", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Expense> expenses = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Setter
    @JsonIgnore
    private User user;

    public void addExpense(Expense expense){
        this.expenses.add(expense);
        expense.setBudgetList(this);
        this.remaining_budget = this.remaining_budget.subtract(expense.getAmount());
    }

    public void updateExpenseAmount(Expense expense, BigDecimal newAmount){
        BigDecimal delta;
        if (newAmount.compareTo(expense.getAmount()) > 0){
            delta = newAmount.subtract(expense.getAmount());
            this.remaining_budget = this.remaining_budget.subtract(delta);
        } else if (newAmount.compareTo(expense.getAmount()) < 0){
                delta = expense.getAmount().subtract(newAmount);
                this.remaining_budget = this.remaining_budget.add(delta);
        }
        expense.setAmount(newAmount);
    }

    public void updateBudgetAfterExpenseDelete(Expense expense){
        this.remaining_budget = this.remaining_budget.add(expense.getAmount());
    }

}
