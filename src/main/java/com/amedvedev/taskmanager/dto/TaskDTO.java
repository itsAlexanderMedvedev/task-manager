package com.amedvedev.taskmanager.dto;

import com.amedvedev.taskmanager.validation.PresentOrFutureDate;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class TaskDTO {

    private Long id;

    @Column(length = 20)
    @Size(max = 20, message = "Maximum 20 characters")
    private String name;

    @Column(length = 750)
    @Size(max = 750, message = "Maximum 750 characters")
    private String description;

    private LocalDate dateCreated;

    @PresentOrFutureDate
    private LocalDate dueDate;
    private List<String> categories;
    private String priority;
}
