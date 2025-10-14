package com.example.bus.security;

import java.math.BigDecimal;

public record RegistrationRequest(String email, String password, BigDecimal credit) {}