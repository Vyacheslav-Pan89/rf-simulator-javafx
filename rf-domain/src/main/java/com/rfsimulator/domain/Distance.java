package com.rfsimulator.domain;

public record Distance(double meters) {
    public Distance {
        if (meters < 0) {
            throw new IllegalArgumentException("Meters cannot be negative");
        }
        if (!Double.isFinite(meters)) {
            throw new IllegalArgumentException("Distance cannot be Infinite");
        }
    }
}
