package com.expenseTracker.expensetracker.model;


import com.expenseTracker.expensetracker.common.Validate;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.descriptor.jdbc.TimestampUtcAsOffsetDateTimeJdbcType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "budget_list")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Budget {

    public Budget(String name, BigDecimal budget, LocalDate startDate, LocalDate endDate, BudgetTypes type , boolean active, User user) {
        this.name = Validate.text(name, "name");
        this.budget = Validate.positive(budget,"budget");
        this.remainingBudget = budget;
        this.startDate = Validate.notNull(startDate,"start date");
        this.endDate = Validate.notNull(endDate, "end date");
        Validate.dateRange(startDate,endDate);
        this.type = Validate.notNull(type, "type");
        this.active = active;
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

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL)
    private List<Expense> expenses = new ArrayList<>();

    @Column(name = "active")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void assignExpense(Expense expense){
        if (this.type == BudgetTypes.STRICT){
            strictBudgetValidation(expense.getAmount());
        } else if (this.type == BudgetTypes.FLEX){
            flexBudgetValidation(expense.getAmount());
        }
        expense.assignToBudget(this);
        this.expenses.add(expense);
        this.remainingBudget = this.remainingBudget.subtract(expense.getAmount());
    }

    public void updateExpenseAmount(Expense expense, BigDecimal newAmount){
        if (newAmount == null){
            throw new IllegalArgumentException("Amount must not be null");
        }

        BigDecimal simulated = this.remainingBudget.add(expense.getAmount()).subtract(newAmount);

        if (this.type == BudgetTypes.STRICT){
            strictValidateSimulated(simulated);
        } else if (this.type == BudgetTypes.FLEX){
            flexValidateSimulated(simulated);
        }
        this.remainingBudget = simulated;
        expense.editAmount(newAmount);

    }
    public void activate(){
        this.active = true;
    }
    public void deactivate(){
        this.active = false;
    }
    public void updateBudgetAfterExpenseDelete(Expense expense){
        this.remainingBudget = this.remainingBudget.add(expense.getAmount());
    }

    private void strictValidateSimulated(BigDecimal simulatedRemaining){
        if (simulatedRemaining.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("You cannot spend over budget limit");
        }
    }

    private void flexValidateSimulated(BigDecimal simulatedRemaining){
        BigDecimal tenPercent = this.budget.multiply(new BigDecimal("0.10"));
        if (simulatedRemaining.compareTo(tenPercent.negate()) < 0){
            throw new IllegalArgumentException("Flex budget allows max 10% over limit");
        }
    }

    private void strictBudgetValidation(BigDecimal amount){
        if (amount.compareTo(this.remainingBudget) > 0){
            throw new IllegalArgumentException("You cannot spent over budget limit. We suggest you to switch to FLEX budget");
        }
    }
    private void flexBudgetValidation(BigDecimal amount){
        BigDecimal tenPercent = this.budget.multiply(new BigDecimal("0.10"));
        BigDecimal newAmount = remainingBudget.subtract(amount);
        if (newAmount.compareTo(tenPercent.negate()) < 0){
            throw new IllegalArgumentException("Flex budget allows max 10% over limit");
        }
    }

}
