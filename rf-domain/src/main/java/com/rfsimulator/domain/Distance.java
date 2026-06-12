package com.rfsimulator.domain;

public record Distance(double meters) {
    public Distance {

        if (!Double.isFinite(meters)) {
            throw new IllegalArgumentException("Meters must be finite");
        }

        if (meters < 0.0) {
            throw new IllegalArgumentException("Meters cannot be negative");
        }

        if (meters == 0.0) {
            meters = 0.0;
        }
    }
}
