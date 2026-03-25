package com.expenseTracker.expensetracker;

import com.expenseTracker.expensetracker.dto.BudgetExpenseDto;
import com.expenseTracker.expensetracker.model.BudgetList;
import com.expenseTracker.expensetracker.model.Expense;
import com.expenseTracker.expensetracker.model.Role;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.repository.BudgetListRepository;
import com.expenseTracker.expensetracker.repository.ExpensiveRepository;
import com.expenseTracker.expensetracker.repository.RoleRepository;
import com.expenseTracker.expensetracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
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
	private BudgetListRepository budgetListRepository;

	@Autowired
	private ExpensiveRepository expensiveRepository;


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
	@Transactional
	void assignRole(){
		Role role = roleRepository.findById(2L).orElseThrow();
		User user = userRepository.findById(2L).orElseThrow();
		Set<Role> roles = user.getRoles();
		roles.add(role);
		userRepository.save(user);
		logger.info("User assigned role: {}", user);
	}




	@Test
	void createBudget(){
		BudgetList budgetList = new BudgetList("Mart Plata", LocalDate.now(),LocalDate.now().plusDays(30));
		budgetListRepository.save(budgetList);
	}

	@Test
	void addExpenseToList(){
		BudgetList budgetList = budgetListRepository.findBudgetById(2L);
		Expense expense = new Expense("Popravka sijelica na lijevi far + dijagnostika", 13,LocalDateTime.now(),"OBAVEZNO", budgetList);
		budgetList.getExpenses().add(expense);
		budgetListRepository.save(budgetList);
		logger.info("Expense added: {}", budgetList);
	}

	@Test
	void selectExpensiveFromList(){
		BudgetList  budgetList = budgetListRepository.findBudgetById(2L);
		List<String> expensiveNames = budgetList.getExpenses().stream().map(Expense::getName).toList();
		logger.info("Budget list: {}", new BudgetExpenseDto(budgetList.getName(),expensiveNames));
	}
}
