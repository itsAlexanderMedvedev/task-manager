package com.amedvedev.taskmanager.repositories;

import com.amedvedev.taskmanager.entities.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
