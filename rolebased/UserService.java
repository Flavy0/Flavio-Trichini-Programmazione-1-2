package com.example.rolebased;

import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final List<User> USERS = Arrays.asList(
            new User("admin", "password", Role.ADMIN),
            new User("user", "password", Role.CUSTOMER)
    );

    public Optional<User> findByUsername(String username) {
        return USERS.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
}