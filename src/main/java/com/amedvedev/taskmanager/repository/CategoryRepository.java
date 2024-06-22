package com.amedvedev.taskmanager.repository;

import com.amedvedev.taskmanager.entitiy.Category;
import com.amedvedev.taskmanager.entitiy.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CategoryRepository extends CrudRepository<Category, String> {
    Category findByUserAndName(User user, String name);
    List<Category> findAllByUser(User user);

    @Query("SELECT c FROM Category c WHERE c.user = :user AND c.name IN :names")
    List<Category> findAllByUserAndNames(User user, List<String> names);
}
