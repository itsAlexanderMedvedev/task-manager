package com.amedvedev.taskmanager.controller;

import com.amedvedev.taskmanager.service.ClientService;
import org.springframework.stereotype.Controller;

@Controller
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
}
