package com.amedvedev.taskmanager.controller;

import com.amedvedev.taskmanager.entitiy.Category;
import com.amedvedev.taskmanager.entitiy.User;
import com.amedvedev.taskmanager.repository.CategoryRepository;
import com.amedvedev.taskmanager.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<Iterable<String>> categories() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(categoryService.findAllByUser(user));
    }

    @PostMapping("/categories")
    public ResponseEntity<String> createCategory(@RequestParam String name) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category category = categoryService.createCategory(name, user);
        return ResponseEntity.ok(category.getName());
    }
}
