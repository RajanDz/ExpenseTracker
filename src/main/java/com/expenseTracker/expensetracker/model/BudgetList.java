package com.expenseTracker.expensetracker.model;


import com.expenseTracker.expensetracker.common.Validate;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetList {

    public BudgetList(String name, BigDecimal budget, LocalDate startDate, LocalDate endDate, User user) {
        this.name = Validate.text(name, "name");
        this.budget = Validate.positive(budget,"budget");
        this.remainingBudget = budget;
        this.startDate = Validate.notNull(startDate,"start date");
        this.endDate = Validate.notNull(endDate, "end date");
        this.user = Validate.notNull(user,"user");
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private  String name;

    @Column(name = "budget")
    private BigDecimal budget;

    @Column(name = "remaining_budget")
    private BigDecimal remainingBudget;

    @Column(name = "start_date")
    private  LocalDate startDate;

    @Column(name = "end_date")
    private   LocalDate endDate;

    @OneToMany(mappedBy = "budgetList", cascade = CascadeType.ALL)
    private List<Expense> expenses = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void addExpense(Expense expense){
        this.expenses.add(expense);
        expense.setBudgetList(this);
        this.remainingBudget = this.remainingBudget.subtract(expense.getAmount());
    }

    public void updateExpenseAmount(Expense expense, BigDecimal newAmount){
        BigDecimal delta;
        if (newAmount.compareTo(expense.getAmount()) > 0){
            delta = newAmount.subtract(expense.getAmount());
            this.remainingBudget = this.remainingBudget.subtract(delta);
        } else if (newAmount.compareTo(expense.getAmount()) < 0){
                delta = expense.getAmount().subtract(newAmount);
                this.remainingBudget = this.remainingBudget.add(delta);
        }
        expense.setAmount(newAmount);
    }

    public void updateBudgetAfterExpenseDelete(Expense expense){
        this.remainingBudget = this.remainingBudget.add(expense.getAmount());
    }

}
