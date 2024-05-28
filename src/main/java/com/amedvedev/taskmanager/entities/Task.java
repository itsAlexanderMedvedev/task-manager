package com.amedvedev.taskmanager.entities;

import com.amedvedev.taskmanager.validation.PresentOrFutureDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    @Size(max = 20, message = "maximum 20 characters")
    private String name;

    @Column(length = 500)
    @Size(max = 500, message = "maximum 500 characters")
    private String description;

    private LocalDate dateCreated;

    @PresentOrFutureDate
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Task(String name, String description, LocalDate dueDate) {
        this.name = name;
        this.description = description;
        this.dateCreated = LocalDate.now();
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
