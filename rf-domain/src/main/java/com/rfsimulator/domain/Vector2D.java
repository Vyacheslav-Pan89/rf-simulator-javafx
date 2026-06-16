package com.rfsimulator.domain;

public record Vector2D(double deltaXMeters, double deltaYMeters) {
    public Vector2D {
        if (!Double.isFinite(deltaXMeters) || !Double.isFinite(deltaYMeters)) {
            throw new IllegalArgumentException("Vector components must be finite");
        }
        if (deltaXMeters == 0.0) {
            deltaXMeters = 0.0;
        }
        if (deltaYMeters == 0.0) {
            deltaYMeters = 0.0;
        }
    }

    public Distance magnitude() {
        double magnitudeMeters = Math.hypot(deltaXMeters, deltaYMeters);
        return new Distance(magnitudeMeters);
    }
}
