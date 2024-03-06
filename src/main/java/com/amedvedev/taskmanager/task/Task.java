package com.amedvedev.taskmanager.task;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Size(min = 5)
    private String description;

    private LocalDate dateCreated;

    @PresentOrFutureDate
    private LocalDate dueDate;

    public Task(String name, String description, LocalDate dueDate) {
        this.name = name;
        this.description = description;
        this.dateCreated = LocalDate.now();
        this.dueDate = dueDate;
        System.out.println("Constructor used");
    }

    private void setId(Long id) {
        // dummy
    }

    public void setDueDate(LocalDate dueDate) {
        System.out.println("Setter called");
        this.dueDate = dueDate;
    }

    public void setDateCreated(String date) {
        this.dateCreated = LocalDate.parse(date);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateCreated=" + dateCreated +
                ", dueDate=" + dueDate +
                '}';
    }
}
