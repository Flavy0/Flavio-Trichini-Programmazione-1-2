package com.example.rolebased;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

    private final UserService userService;

    public Controller(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/public")
    public String publicAccess() {
        return "Ciao a tutti! Questa rotta è pubblica.";
    }

    @GetMapping("/admin/{username}")
    public String adminAccess(@PathVariable String username) {
        return userService.findByUsername(username)
                .filter(user -> user.getRole() == Role.ADMIN)
                .map(user -> "Benvenuto, " + user.getUsername() + "Sei un amministratore e puoi accedere a questa rotta")
                .orElse("Accesso negato");
    }
}