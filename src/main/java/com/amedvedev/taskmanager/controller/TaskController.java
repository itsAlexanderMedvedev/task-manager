package com.amedvedev.taskmanager.controller;

import com.amedvedev.taskmanager.dto.FilterDTO;
import com.amedvedev.taskmanager.dto.TaskDTO;
import com.amedvedev.taskmanager.dto.TaskSummaryDTO;
import com.amedvedev.taskmanager.service.TaskService;
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

    @GetMapping("/sort")
    public ResponseEntity<List<TaskSummaryDTO>> sortTasks(@RequestParam(required = false) String sort,
                                                          @RequestParam(required = false) String order,
                                                          @ModelAttribute FilterDTO filterDTO) {

        var data = taskService.findAll(sort, order, filterDTO);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TaskSummaryDTO>> filterTasks(@ModelAttribute FilterDTO filterDTO) {
        var data = taskService.findAll(filterDTO);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskSummaryDTO>> searchTasks(@RequestParam String term) {
        var data = taskService.search(term);
        return ResponseEntity.ok(data);
    }
}
