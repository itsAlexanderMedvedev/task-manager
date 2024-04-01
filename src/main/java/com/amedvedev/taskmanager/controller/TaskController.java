package com.amedvedev.taskmanager.controller;

import com.amedvedev.taskmanager.service.TaskService;
import com.amedvedev.taskmanager.task.Task;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String tasks(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "/tasks";
    }

    @PostMapping("/tasks/submit-task")
    public String handleForm(@Valid @ModelAttribute Task task, BindingResult result, Model model) {
        System.out.println(task.getId());
        if(!result.hasErrors()) {
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

    @PostMapping("/tasks/saveTask")
    @ResponseBody
    public ResponseEntity<?> saveTask(@Valid @RequestBody Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        taskService.save(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/tasks/editTask/{id}")
    @ResponseBody
    public Task editTask(@PathVariable Long id) {
        return taskService.find(id);
    }
}
