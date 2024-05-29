package com.amedvedev.taskmanager.repositories;

import com.amedvedev.taskmanager.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
