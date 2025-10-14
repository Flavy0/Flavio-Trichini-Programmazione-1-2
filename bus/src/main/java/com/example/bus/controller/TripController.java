package com.example.bus.controller;

import com.example.bus.model.Trip;
import com.example.bus.security.InsufficientCreditException;
import com.example.bus.security.Receipt;
import com.example.bus.service.TripService;
import com.example.bus.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trips")
public class TripController {
    private final TripService tripService;
    private final UserService userService;

    public TripController(TripService tripService, UserService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

    // GET /trips (PUBBLICO)
    @GetMapping
    public List<Trip> getAllTrips() {
        return tripService.findAll();
    }

    // POST /trips (SOLO ADMIN)
    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody Trip trip) {
        Trip newTrip = tripService.save(trip);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTrip);
    }

    // POST /trips/{tripId}/buy (AUTENTICATO)
    @PostMapping("/{tripId}/buy")
    public ResponseEntity<?> buyTrip(@PathVariable Long tripId, Principal principal) {
        Long userId = userService.findByEmail(principal.getName()).orElseThrow().getId();

        try {
            Receipt receipt = userService.buyTrip(userId, tripId);
            return ResponseEntity.ok(receipt);
        } catch (InsufficientCreditException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of("error", e.getMessage()));
        }
    }
}