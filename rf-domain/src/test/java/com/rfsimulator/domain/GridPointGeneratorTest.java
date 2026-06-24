package com.rfsimulator.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GridPointGeneratorTest {
    @Test
    public void generatesExpectedSampleCount() {
        int xSamples = 2;
        int ySamples = 2;
        int expectedSampleCount = xSamples * ySamples;

        RectangularGridDefinition gridDefinition = new RectangularGridDefinition(
                new Position2D(0.0, 0.0),
                new Position2D(10.0, 10.0),
                xSamples, ySamples);
        List<Position2D> position2DS = new GridPointGenerator().generate(gridDefinition);

        assertEquals(expectedSampleCount, position2DS.size());
    }

    // Focuses on boundary inclusion without repeating the full row-major list.
    @Test
    public void includesMinimumAndMaximumCorners() {
        RectangularGridDefinition gridDefinition = new RectangularGridDefinition(
                new Position2D(0.0, 0.0),
                new Position2D(10.0, 10.0),
                2, 2);
        List<Position2D> position2DS = new GridPointGenerator().generate(gridDefinition);

        assertEquals(new Position2D(0.0, 0.0), position2DS.getFirst());
        assertEquals(new Position2D(10.0, 10.0), position2DS.getLast());
    }

    // A 3 x 3 grid shows both row-major ordering and interior derived-spacing
    // points.
    @Test
    public void generatesPointsInRowMajorOrder() {
        List<Position2D> expectedPoints = List.of(
                new Position2D(0, 0),
                new Position2D(5, 0),
                new Position2D(10, 0),
                new Position2D(0, 5),
                new Position2D(5, 5),
                new Position2D(10, 5),
                new Position2D(0, 10),
                new Position2D(5, 10),
                new Position2D(10, 10));

        RectangularGridDefinition gridDefinition = new RectangularGridDefinition(
                new Position2D(0.0, 0.0),
                new Position2D(10.0, 10.0),
                3, 3);
        List<Position2D> positions = new GridPointGenerator().generate(gridDefinition);

        assertEquals(expectedPoints, positions);
    }

    // Non-square grids keep x and y sample counts independent.
    @Test
    public void generatesNonSquareGridPoints() {
        RectangularGridDefinition gridDefinition = new RectangularGridDefinition(
                new Position2D(0.0, 0.0),
                new Position2D(10.0, 10.0),
                3, 2);
        List<Position2D> position2DS = new GridPointGenerator().generate(gridDefinition);

        assertEquals(new Position2D(0.0, 0.0), position2DS.getFirst());
        assertEquals(new Position2D(10.0, 0.0), position2DS.get(2));
        assertEquals(new Position2D(10.0, 10.0), position2DS.getLast());
    }

    // Checks derived spacing directly instead of relying only on the full
    // row-major expected list.
    @Test
    public void usesDerivedSpacingBetweenBounds() {
        RectangularGridDefinition gridDefinition = new RectangularGridDefinition(
                new Position2D(0.0, 0.0),
                new Position2D(10.0, 10.0),
                3, 3);
        List<Position2D> position2DS = new GridPointGenerator().generate(gridDefinition);
        double xStep = (10.0 - 0.0) / (3 - 1);
        double yStep = (10.0 - 0.0) / (3 - 1);

        assertEquals(xStep, position2DS.get(1).xMeters() - position2DS.get(0).xMeters());
        assertEquals(yStep, position2DS.get(3).yMeters() - position2DS.get(0).yMeters());
    }

    @Test
    public void rejectsNullGridDefinition() {
        assertThrows(NullPointerException.class, () ->
                new GridPointGenerator().generate(null));
    }

    @Test
    public void returnsImmutablePointList() {
        RectangularGridDefinition gridDefinition = new RectangularGridDefinition(
                new Position2D(0.0, 0.0),
                new Position2D(10.0, 10.0),
                3, 3);
        List<Position2D> position2DS = new GridPointGenerator().generate(gridDefinition);

        assertThrows(UnsupportedOperationException.class, position2DS::removeFirst);
    }

    @Test
    public void generateLargeResolutionGrid() {
        RectangularGridDefinition gridDefinition = new RectangularGridDefinition(
                new Position2D(0.0, 0.0),
                new Position2D(10.0, 10.0),
                100, 50);

        List<Position2D> positions = new GridPointGenerator().generate(gridDefinition);

        assertEquals(5000, positions.size());
        assertEquals(new Position2D(0.0, 0.0), positions.getFirst());
        assertEquals(new Position2D(10.0, 10.0), positions.getLast());
    }
}
