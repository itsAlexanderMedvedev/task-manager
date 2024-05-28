package com.amedvedev.taskmanager.repositories;

import com.amedvedev.taskmanager.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
