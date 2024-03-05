package com.amedvedev.taskmanager.controller;

import com.amedvedev.taskmanager.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String hello(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks";
    }
}
