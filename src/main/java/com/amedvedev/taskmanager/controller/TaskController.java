package com.amedvedev.taskmanager.controller;

import com.amedvedev.taskmanager.service.TaskService;
import com.amedvedev.taskmanager.task.Task;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String tasks(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("task", new Task());
        return "/tasks";
    }

    @PostMapping("/tasks/submit-task")
    public String handleForm(@Valid @ModelAttribute Task task, BindingResult result, Model model) {
        System.out.println("controller entered");
        System.out.println(task.getDescription());
        if(!result.hasErrors()) {
            System.out.println(!task.getDueDate().isBefore(LocalDate.now()));
            task.setDateCreated(LocalDate.now().toString());
            taskService.save(task);
            System.out.println(task + " saved");
            model.addAttribute("tasks", taskService.findAll());
            return "redirect:/tasks";
        }
        model.addAttribute("tasks", taskService.findAll());
        return "/tasks";
    }

    @PostMapping("/tasks/delete")
    public String deleteTask(@RequestParam Long id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }

    @GetMapping("/clear")
    public String clear() {
        taskService.deleteAll();
        return "redirect:/tasks";
    }
}
