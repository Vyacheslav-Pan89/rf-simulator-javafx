package com.rfsimulator.domain;

public record PowerWatts(double watts) {

    public PowerWatts {

        if (!Double.isFinite(watts)) {
            throw new IllegalArgumentException("Watts must be finite");
        }

        if (watts < 0.0) {
            throw new IllegalArgumentException("Watts cannot be negative");
        }

        if (watts == 0.0) {
            watts = 0.0;
        }

    }

}
