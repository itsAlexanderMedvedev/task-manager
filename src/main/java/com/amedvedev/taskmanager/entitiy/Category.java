package com.amedvedev.taskmanager.entitiy;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {

    @Id
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder.Default
    @ManyToMany(mappedBy = "categories")
    private Set<Task> tasks = new HashSet<>();
}
