package com.expenseTracker.expensetracker.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role")
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NonNull
    private String name;

    @Column(name = "description")
    @NonNull
    private String description;
}
