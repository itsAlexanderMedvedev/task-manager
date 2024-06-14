package com.amedvedev.taskmanager.controller;

import com.amedvedev.taskmanager.dto.TaskDTO;
import com.amedvedev.taskmanager.dto.TaskSummaryDTO;
import com.amedvedev.taskmanager.entitiy.Category;
import com.amedvedev.taskmanager.repository.CategoryRepository;
import com.amedvedev.taskmanager.service.TaskService;
import com.amedvedev.taskmanager.entitiy.Task;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/sort")
    public ResponseEntity<List<TaskSummaryDTO>> sortTasks(@RequestParam(required = false) String sort,
                                                          @RequestParam(required = false) String order) {
        return ResponseEntity.ok(taskService.findAll(sort, order));
    }

    @GetMapping
    public String tasks(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public TaskDTO findTaskById(@PathVariable Long id) {
        return taskService.find(id);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> saveTask(@Valid @RequestBody TaskDTO taskDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        taskService.save(taskDTO);
        return new ResponseEntity<>(taskDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        try {
            taskService.delete(id);
            return ResponseEntity.ok("Record deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Failed to delete record with id " + id + ": " + e.getMessage()
            );
        }
    }
}
