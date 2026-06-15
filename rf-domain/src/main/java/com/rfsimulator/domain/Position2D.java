package com.rfsimulator.domain;

public record Position2D(double xMeters, double yMeters) {
    public Position2D {
        if (!Double.isFinite(xMeters) || !Double.isFinite(yMeters)) {
            throw new IllegalArgumentException("Coordinates must be finite");
        }

        if (xMeters == 0.0) {
            xMeters = 0.0;
        }
        if (yMeters == 0.0) {
            yMeters = 0.0;
        }
    }
}
