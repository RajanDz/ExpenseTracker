package com.expenseTracker.expensetracker;

import com.expenseTracker.expensetracker.model.Role;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.repository.RoleRepository;
import com.expenseTracker.expensetracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(classes = ExpensetrackerApplication.class)
class ExpensetrackerApplicationTests {

	private static Logger logger = LoggerFactory.getLogger(ExpensetrackerApplication.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

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
}
