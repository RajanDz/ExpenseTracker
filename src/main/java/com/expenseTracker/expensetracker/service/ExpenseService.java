package com.expenseTracker.expensetracker.service;


import com.expenseTracker.expensetracker.dto.CreateExpenseDto;
import com.expenseTracker.expensetracker.dto.ExpenseDetailsResponse;
import com.expenseTracker.expensetracker.dto.ExpenseResponse;
import com.expenseTracker.expensetracker.dto.UpdateExpenseFields;
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
        List<String> categories = Arrays.stream(Category.values())
                .map(category -> category.name())
                .toList();
        return categories;
    }
    public List<ExpenseResponse> getExpenseListByFilters(Boolean price, String category,User user,Long budgetId , Pageable pageable){
        Budget budget = budgetRepository.findByIdAndUserId(budgetId,user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Budget not found"));
        Specification<Expense> expenseList = findExpenseByFilters(price,category,budget);
        return expenseRepository.findAll(expenseList,pageable)
                .map(expense -> new ExpenseResponse(expense.getId(),expense.getName(),expense.getAmount(),expense.getCategory())).toList();
    }
    public Specification<Expense> findExpenseByFilters(Boolean price, String category, Budget budget){
        return((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get("budget"),budget));
            if (price != null && price) {
                query.orderBy(criteriaBuilder.asc(root.get("amount")));
            }
            if (category != null && !category.isEmpty()){
                predicateList.add(criteriaBuilder.equal(root.get("category"),Category.valueOf(category)));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));

        });
    }
}
