package com.amedvedev.taskmanager.repositories;

import com.amedvedev.taskmanager.entities.Task;
import com.amedvedev.taskmanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserUsername(String username);
}
