package com.expenseTracker.expensetracker.model;


import com.expenseTracker.expensetracker.common.Validate;
import com.expenseTracker.expensetracker.dto.BudgetTypes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "budget_list")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetList {

    public BudgetList(String name, BigDecimal budget, LocalDate startDate, LocalDate endDate, BudgetTypes type ,User user) {
        this.name = Validate.text(name, "name");
        this.budget = Validate.positive(budget,"budget");
        this.remainingBudget = budget;
        this.startDate = Validate.notNull(startDate,"start date");
        this.endDate = Validate.notNull(endDate, "end date");
        this.type = Validate.notNull(type, "type");
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
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private BudgetTypes type;

    @OneToMany(mappedBy = "budgetList", cascade = CascadeType.ALL)
    private List<Expense> expenses = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void addExpense(Expense expense){
        if (this.type == BudgetTypes.STRICT){
            strictBudgetValidation(expense.getAmount());
        } else if (this.type == BudgetTypes.FLEX){
            flexBudgetValidation(expense.getAmount());
        }
        this.expenses.add(expense);
        expense.setBudgetList(this);
        this.remainingBudget = this.remainingBudget.subtract(expense.getAmount());
    }

    public void updateExpenseAmount(Expense expense, BigDecimal newAmount){
        BigDecimal delta;
        if (newAmount == null){
            throw new IllegalArgumentException("Amount must not be null");
        }

        if (newAmount.compareTo(expense.getAmount()) > 0){
            delta = newAmount.subtract(expense.getAmount());
            this.remainingBudget = this.remainingBudget.subtract(delta);

        } else if (newAmount.compareTo(expense.getAmount()) < 0){
                delta = expense.getAmount().subtract(newAmount);
                this.remainingBudget = this.remainingBudget.add(delta);
        }
        expense.editAmount(newAmount);
    }

    public void updateBudgetAfterExpenseDelete(Expense expense){
        this.remainingBudget = this.remainingBudget.add(expense.getAmount());
    }

    public void strictBudgetValidation(BigDecimal amount){
        if (amount.compareTo(this.remainingBudget) > 0){
            throw new IllegalArgumentException("You cannot spent over budget limit. We suggest you to switch to FLEX budget");
        }
    }
    public void flexBudgetValidation(BigDecimal amount){
        BigDecimal tenPercent = budget.multiply(new BigDecimal("0.10"));
        BigDecimal newAmount = remainingBudget.subtract(amount);
        if (newAmount.compareTo(tenPercent.negate()) < 0){
            throw new IllegalArgumentException("Flex budget allows max 10% over limit");
        }
    }

}
