package com.example.bus.service;

import com.example.bus.entity.TripRepository;
import com.example.bus.model.Trip;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;

    // Iniezione del repository tramite costruttore
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    // Metodo per l'endpoint GET /trips
    public List<Trip> findAll() {
        // Cerca tutte le corse nel database
        return tripRepository.findAll();
    }

    // Metodo per l'endpoint POST /trips (solo ADMIN)
    public Trip save(Trip trip) {
        return tripRepository.save(trip);
    }
}
