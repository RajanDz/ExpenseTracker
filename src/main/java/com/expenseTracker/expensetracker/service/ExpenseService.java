package com.expenseTracker.expensetracker.service;


import com.expenseTracker.expensetracker.dto.*;
import com.expenseTracker.expensetracker.model.Budget;
import com.expenseTracker.expensetracker.model.Category;
import com.expenseTracker.expensetracker.model.Expense;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.repository.BudgetRepository;
import com.expenseTracker.expensetracker.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);

    public ExpenseDetailsResponse getExpenseById(User user, Long budgetId, Long expenseId){
        Budget budget = budgetRepository.findByIdAndUserId(budgetId,user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Budget not found"));

        return expenseRepository.findByIdAndBudgetId(expenseId,budget.getId())
                .map(expense ->
                        new ExpenseDetailsResponse(
                                expense.getId(),
                                expense.getName()
                                ,expense.getAmount()
                                ,expense.getDateTime()
                                ,expense.getCategory()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
    }

    @Transactional
    public Budget addExpenseToBudgetList(CreateExpenseDto createExpense, User user){
        Budget budget = budgetRepository.findByIdAndUserId(createExpense.getBudgetId(), user.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget not found with id: " + createExpense.getBudgetId())
        );
        Expense expense = Expense.createExpense(createExpense.getTitle(),createExpense.getAmount(),createExpense.getCategory());
        budget.assignExpense(expense);
        budgetRepository.save(budget);
        return budget;
    }

    @Transactional
    public void deleteExpense(long expenseId, User user){
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
        Budget budget = budgetRepository.findByIdAndUserId(expense.getBudget().getId(),user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
        budget.updateBudgetAfterExpenseDelete(expense);
        expenseRepository.delete(expense);
    }

    @Transactional
    public Expense updateExpenseFields(UpdateExpenseFields expenseFields, User user){
        if (expenseFields.getName() == null && expenseFields.getCategory() == null && expenseFields.getAmount() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No fields to update");
        }
        Budget budget = budgetRepository.findByIdAndUserId(expenseFields.getBudgetId(),user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget not found with providede userId"));
        Expense expense = expenseRepository.findByIdAndBudgetId(expenseFields.getExpenseId(), budget.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found with id: " + expenseFields.getExpenseId()));
            if (expenseFields.getName() != null){
                expense.editName(expenseFields.getName());
            }
            if (expenseFields.getCategory() != null){
                expense.editCategory(expenseFields.getCategory());
            }
            if (expenseFields.getAmount() != null){
                budget.updateExpenseAmount(expense,expenseFields.getAmount());
            }
        expenseRepository.save(expense);
        return expense;
    }

    public List<String> getAllCategories(){
        return Arrays.stream(Category.values())
                .map(Category::name)
                .toList();
    }
    public List<ExpenseResponse> getExpenseListByFilters(User user, ExpenseSearchFilters expenseSearchFilters, Pageable pageable){
        Budget budget = budgetRepository.findByIdAndUserId(expenseSearchFilters.budgetId(),user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Budget not found"));
        Specification<Expense> expenseList = findExpenseByFilters(expenseSearchFilters.amountSort(),expenseSearchFilters.category(),expenseSearchFilters.fromDate(),expenseSearchFilters.toDate(),budget);
        return expenseRepository.findAll(expenseList,pageable)
                .map(expense -> new ExpenseResponse(expense.getId(),expense.getName(),expense.getAmount(),expense.getCategory(),expense.getDateTime())).toList();
    }
    public Specification<Expense> findExpenseByFilters(String price, String category, LocalDate fromDate, LocalDate toDate, Budget budget){
        return((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get("budget"),budget));
            if (fromDate != null){
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateTime"), fromDate.atStartOfDay()));
            }
            if (toDate != null){
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateTime"), toDate.atTime(23,59,59)));
            }
            if (category != null && !category.isEmpty()){
                predicateList.add(criteriaBuilder.equal(root.get("category"),Category.valueOf(category)));
            }
            if ("ASC".equals(price)) {
                query.orderBy(criteriaBuilder.asc(root.get("amount")));
            } else if ("DESC".equals(price)){
                query.orderBy(criteriaBuilder.desc(root.get("amount")));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));

        });
    }
}
