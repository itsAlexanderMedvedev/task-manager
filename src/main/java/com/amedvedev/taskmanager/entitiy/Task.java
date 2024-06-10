package com.amedvedev.taskmanager.entitiy;

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

    @Column(length = 750)
    @Size(max = 750, message = "maximum 750 characters")
    private String description;

    private LocalDate dateCreated;

    @PresentOrFutureDate
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_user_username")
    private User user;

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
                ", user=" + user +
                '}';
    }
}
