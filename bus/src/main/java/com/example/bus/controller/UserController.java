package com.example.bus.controller;

import com.example.bus.model.User;
import com.example.bus.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    record TopUpRequest(BigDecimal amount) {}
    record BuyRequest(Long userId) {}

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserDetails(@PathVariable Long id) {
        return userService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/credit/topup")
    public ResponseEntity<User> topUpCredit(@PathVariable Long id, @RequestBody TopUpRequest request) {
        try {
            if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body(null);
            }

            User updatedUser = userService.topUpCredit(id, request.amount());
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}