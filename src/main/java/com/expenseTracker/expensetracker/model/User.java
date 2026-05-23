package com.expenseTracker.expensetracker.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NonNull
    private String name;

    @Column(name = "lastname")
    @NonNull
    private String lastname;

    @Column(name = "username")
    @NonNull
    private String username;

    @Column(name = "email")
    @NonNull
    private String email;

    @Column(name = "password")
    @NonNull
    private String password;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Budget> budgets = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public void createBudget(Budget budget){
        this.budgets.add(budget);
    }

    public void editUsername(String username){
        if (!username.matches(".*[A-Z].*")){
            throw new IllegalArgumentException("Username must have at least one uppercase character");
        }
        if (!username.matches(".*[0-9].*")){
            throw  new IllegalArgumentException("Username must have at least one number");
        }
        this.username = username;
    }

    public void editEmail(String email){
        this.email = email.toLowerCase();
    }

    public void validatePassword(String password){
        if (!password.matches(".*[A-Z].*")){
            throw new IllegalArgumentException("Password must have at least one upper case character.");
        }
        if (!password.matches(".*[0-9].*")){
            throw new IllegalArgumentException("Password must have at least one number.");
        }
    }
    public void editPassword(String encodedPassword){
        this.password = encodedPassword;
    }
}
