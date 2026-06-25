package com.expenseTracker.expensetracker;

import com.expenseTracker.expensetracker.dto.BudgetExpenseDto;
import com.expenseTracker.expensetracker.model.*;
import com.expenseTracker.expensetracker.repository.BudgetRepository;
import com.expenseTracker.expensetracker.repository.ExpenseRepository;
import com.expenseTracker.expensetracker.repository.RoleRepository;
import com.expenseTracker.expensetracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest(classes = ExpensetrackerApplication.class)
class ExpensetrackerApplicationTests {

	private static Logger logger = LoggerFactory.getLogger(ExpensetrackerApplication.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private BudgetRepository budgetRepository;

	@Autowired
	private ExpenseRepository expensiveRepository;


	@Test
	void contextLoads() {
	}


	@Test
	void createUser(){
		User user = new User("Movsar", "Evloev", "evloev2026","movsarevloev@gmail.com" , Objects.requireNonNull(passwordEncoder.encode("evloev123")));
		userRepository.save(user);
		logger.info("User created: {}", user);
	}

	@Test
	void createRole(){
		Role role = new Role("User", "User access");
		roleRepository.save(role);
		logger.info("Role created: {}", role);
	}

	@Test
	void assignRole(){
		Role role = roleRepository.findById(2L).orElseThrow();
		User user = userRepository.findByUsername("evloev2026").orElseThrow();
		Set<Role> roles = user.getRoles();
		roles.add(role);
		userRepository.save(user);
		logger.info("User assigned role: {}", user);
	}



//
//	@Test
//	void createBudget(){
//		BudgetList budgetList = BudgetList.builder()
//				.name("Potpuna stednja")
//				.startDate(LocalDate.now())
//				.endDate(LocalDate.now()
//				.plusDays(15))
//				.build();
//		budgetListRepository.save(budgetList);
//		logger.info("Budget: {}", budgetList);
//	}

	@Test
	void addExpenseToList(){
		Budget budget = budgetRepository.findBudgetById(4L).orElseThrow();
		Expense expense = Expense.builder()
				.name("Patike")
				.amount(BigDecimal.valueOf(45))
				.dateTime(LocalDateTime.now())
				.category(Category.IMPORTANT)
				.build();
		budget.assignExpense(expense);
		budgetRepository.save(budget);
		logger.info("Expense added: {}", budget);
	}

	@Test
	void selectExpenseFromList(){
		Budget budget = budgetRepository.findBudgetById(4L).orElseThrow();
		List<String> expensiveNames = budget.getExpenses().stream().map(Expense::getName).toList();
		logger.info("Budget list: {}", new BudgetExpenseDto(budget.getName(),expensiveNames));
	}
}
