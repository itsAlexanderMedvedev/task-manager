package com.amedvedev.taskmanager.service;

import com.amedvedev.taskmanager.entitiy.Category;
import com.amedvedev.taskmanager.entitiy.User;
import com.amedvedev.taskmanager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findByNameAndUser(String name, User user) {
        return categoryRepository.findByNameAndUser(name, user);
    }

    public Iterable<String> findAllByUser(User user) {
        return categoryRepository.findAllByUser(user).stream().map(Category::getName).toList();
    }
}
