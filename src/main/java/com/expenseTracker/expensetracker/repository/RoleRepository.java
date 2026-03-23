package com.expenseTracker.expensetracker.repository;

import com.expenseTracker.expensetracker.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
