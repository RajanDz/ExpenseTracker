package com.expenseTracker.expensetracker.model;


import com.expenseTracker.expensetracker.dto.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "budget_list")
    @Setter
    private BudgetList budgetList;

    public void editName(String name){
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("You need to provide valid name.");
        }
        this.name = name;
    }
    public void editCategory(Category category){
        if (category == null){
            throw new IllegalArgumentException("You need to provide valid category");
        }
        this.category = category;
    }
    public void editAmount(BigDecimal amount){
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        this.amount = amount;
    }
}
