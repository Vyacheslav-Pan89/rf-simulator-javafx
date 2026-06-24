package com.rfsimulator.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class GridPointGenerator {
    public List<Position2D> generate(RectangularGridDefinition gridDefinition) {
        Objects.requireNonNull(gridDefinition, "gridDefinition");

        double xStep = (gridDefinition.maximum().xMeters() -
                gridDefinition.minimum().xMeters()) / (gridDefinition.xSamples() - 1);

        double yStep = (gridDefinition.maximum().yMeters() -
                gridDefinition.minimum().yMeters()) / (gridDefinition.ySamples() - 1);

        final List<Position2D> points = new ArrayList<>();

        for (int y = 0; y < gridDefinition.ySamples(); y++) {
            for (int x = 0; x < gridDefinition.xSamples(); x++) {
                double xMeters = x == gridDefinition.xSamples() - 1
                        ? gridDefinition.maximum().xMeters()
                        : gridDefinition.minimum().xMeters() + x * xStep;

                double yMeters = y == gridDefinition.ySamples() - 1
                        ? gridDefinition.maximum().yMeters()
                        : gridDefinition.minimum().yMeters() + y * yStep;

                points.add(new Position2D(xMeters, yMeters));
            }
        }
        return List.copyOf(points);
    }
}
