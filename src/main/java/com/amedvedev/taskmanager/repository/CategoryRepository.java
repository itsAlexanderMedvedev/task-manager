package com.amedvedev.taskmanager.repository;

import com.amedvedev.taskmanager.entitiy.Category;
import com.amedvedev.taskmanager.entitiy.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface CategoryRepository extends CrudRepository<Category, Long> {
    Category findByNameAndUser(String name, User user);
    List<Category> findAllByUser(User user);
}
