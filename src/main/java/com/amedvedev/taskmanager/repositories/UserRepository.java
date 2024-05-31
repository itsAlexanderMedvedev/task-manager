package com.amedvedev.taskmanager.repositories;

import com.amedvedev.taskmanager.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
