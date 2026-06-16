package com.rfsimulator.domain;

import java.util.Objects;

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

    public Vector2D vectorTo(Position2D other) {
        Objects.requireNonNull(other, "Other cannot be null");
        return new Vector2D(
                other.xMeters - this.xMeters,
                other.yMeters - this.yMeters);
    }

    public Distance distanceTo(Position2D other) {
        return vectorTo(other).magnitude();
    }
}
