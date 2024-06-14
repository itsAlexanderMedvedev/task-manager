package com.amedvedev.taskmanager.dto;

import com.amedvedev.taskmanager.entitiy.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class TaskSummaryDTO {
    private Long id;
    private String name;
    private LocalDate dueDate;
    private String categories;
    private String priority;
}
