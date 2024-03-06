package com.amedvedev.taskmanager.service;

import com.amedvedev.taskmanager.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
