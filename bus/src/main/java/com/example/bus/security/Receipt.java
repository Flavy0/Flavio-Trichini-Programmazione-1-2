package com.example.bus.security;

import java.math.BigDecimal;

public record Receipt(Long userId, Long tripId, BigDecimal debit, BigDecimal remainingBalance) {}