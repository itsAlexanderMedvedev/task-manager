package com.amedvedev.taskmanager.repository;

import com.amedvedev.taskmanager.entitiy.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserUsername(String username, Sort sort);
}
