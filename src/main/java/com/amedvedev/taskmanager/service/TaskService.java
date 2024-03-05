package com.amedvedev.taskmanager.service;

import com.amedvedev.taskmanager.task.Task;

import java.util.List;

public interface TaskService {

    List<Task> findAll();
}
