package com.amedvedev.taskmanager.repository;

import com.amedvedev.taskmanager.entitiy.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByUsernameIgnoreCase(String username);
}
