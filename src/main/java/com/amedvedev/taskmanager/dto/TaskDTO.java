package com.amedvedev.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class TaskDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDate dateCreated;
    private LocalDate dueDate;

}
