package com.example.bus.controller;

import com.example.bus.model.User;
import com.example.bus.service.UserService;
import com.example.bus.security.AuthenticationRequest;
import com.example.bus.security.AuthenticationResponse;
import com.example.bus.security.RegistrationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private static final String USER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.test-jwt-token-per-user";
    private static final String ADMIN_TOKEN = "eyJhbGciOiJIUzI1NiJ9.test-jwt-token-per-admin";

    private final UserService userService;
    private final AuthenticationManager authManager;

    public AuthController(UserService userService, AuthenticationManager authManager ) {
        this.userService = userService;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegistrationRequest request) {
        User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setCredit(request.credit() != null ? request.credit() : null);

        User savedUser = userService.register(newUser, request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {

        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserDetails userDetails = userService.findByEmail(request.email()).orElseThrow();

        String token;
        boolean isAdmin = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ADMIN") || a.equals("ROLE_ADMIN")); // Controlla sia "ADMIN" che "ROLE_ADMIN"

        if (isAdmin) {
            token = ADMIN_TOKEN;
        } else {
            token = USER_TOKEN;
        }

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}