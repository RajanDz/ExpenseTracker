package com.expenseTracker.expensetracker.repository;

import com.expenseTracker.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    @Query("""
    select u from User u
    left join fetch u.roles
    where u.username = :username
""")
    Optional<User> findByUsername(String username);
}
