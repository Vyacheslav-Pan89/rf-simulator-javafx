package com.rfsimulator.domain;

import java.util.Objects;

public record RectangularGridDefinition(Position2D minimum,
                                        Position2D maximum,
                                        int xSamples,
                                        int ySamples) {
    public RectangularGridDefinition {
        Objects.requireNonNull(minimum, "minimum");
        Objects.requireNonNull(maximum, "maximum");

        if (xSamples < 2) {
            throw new IllegalArgumentException("xSamples must be greater than or equal to 2");
        }
        if (ySamples < 2) {
            throw new IllegalArgumentException("ySamples must be greater than or equal to 2");
        }
        if (maximum.xMeters() <= minimum.xMeters()) {
            throw new IllegalArgumentException("Maximum x meters must be greater than Minimum x meters");
        }
        if (maximum.yMeters() <= minimum.yMeters()) {
            throw new IllegalArgumentException("Maximum y meters must be greater than Minimum y meters");
        }
    }
}
