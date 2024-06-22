package com.amedvedev.taskmanager.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return authenticationService.register(request);
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<?> register(@RequestBody LoginRequest request, HttpServletResponse response) {
        return authenticationService.login(request, response);
    }
}
