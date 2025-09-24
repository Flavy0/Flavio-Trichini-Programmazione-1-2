package com.example.rolebased;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

    @GetMapping("/public")
    public String publicAccess() {
        return "Ciao a tutti! Questa rotta è pubblica.";
    }

    @GetMapping("/admin")
    public String adminAccess() {
        return "Benvenuto, sei un amministratore e puoi accedere a questa rotta.";
    }
}