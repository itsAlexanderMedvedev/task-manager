package com.amedvedev.taskmanager.entitiy;

import com.amedvedev.taskmanager.validation.PresentOrFutureDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDate dateCreated;

    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_user_username")
    private User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "task_category",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();
    private String priority;
}
