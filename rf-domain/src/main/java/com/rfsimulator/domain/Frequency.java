package com.rfsimulator.domain;

public record Frequency(double hertz) {

    public Frequency {

        if (!Double.isFinite(hertz)) {
            throw new IllegalArgumentException("Hertz must be finite");
        }
        if (hertz <= 0.0) {
            throw new IllegalArgumentException("Hertz must be positive");
        }
    }

}
