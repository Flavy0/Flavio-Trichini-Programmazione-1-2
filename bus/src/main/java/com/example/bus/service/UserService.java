package com.example.bus.service;

import com.example.bus.entity.TripRepository;
import com.example.bus.entity.UserRepository;
import com.example.bus.exception.ResourceNotFoundException;
import com.example.bus.model.Trip;
import com.example.bus.model.User;
import com.example.bus.security.InsufficientCreditException;
import com.example.bus.security.Receipt;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    // 1. DICHIARAZIONE DELLE VARIABILI
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final PasswordEncoder passwordEncoder;

    // 2. COSTRUTTORE
    public UserService(UserRepository userRepository, TripRepository tripRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // METODI FONDAMENTALI (ACCESSO AI DATI)

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Usato da UserDetailsServiceImpl e dal Controller per il JWT
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ----------------------------------------------------
    // METODI DI BUSINESS
    // ----------------------------------------------------

    // Registrazione
    public User register(User user, String rawPassword) {
        // user.setRoles(Collections.singletonList("USER")); // Il ruolo è settato di default nell'entità User
        user.setPassword(passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }

    // Ricarica credito
    public User topUpCredit(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("L'importo deve essere positivo.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

        user.setCredit(user.getCredit().add(amount));
        return userRepository.save(user);
    }

    // Acquisto corsa
    public Receipt buyTrip(Long userId, Long tripId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Corsa con ID " + tripId + " non trovata.")); // RIGA 74

        BigDecimal price = trip.getPrice();

        if (user.getCredit().compareTo(price) < 0) {
            throw new InsufficientCreditException("Credito insufficiente.");
        }

        user.setCredit(user.getCredit().subtract(price));
        userRepository.save(user);

        return new Receipt(userId, tripId, price, user.getCredit());
    }
}