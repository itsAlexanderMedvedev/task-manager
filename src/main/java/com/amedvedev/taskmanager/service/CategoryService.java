package com.amedvedev.taskmanager.service;

import com.amedvedev.taskmanager.entitiy.Category;
import com.amedvedev.taskmanager.entitiy.User;
import com.amedvedev.taskmanager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findByNameAndUser(String name, User user) {
        return categoryRepository.findByUserAndName(user, name);
    }

    public Iterable<String> findAllByUser(User user) {
        return categoryRepository.findAllByUser(user).stream().map(Category::getName).toList();
    }

    public Category createCategory(String name, User user) {
        Category category = Category.builder()
                .name(name)
                .user(user)
                .build();

        return categoryRepository.save(category);
    }
}
