package com.rfsimulator.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GridPointGeneratorTest {
    @Test
    public void generatesExpectedSampleCount() {
        RectangularGridDefinition gridDefinition = new RectangularGridDefinition(
                new Position2D(0.0, 0.0),
                new Position2D(10.0, 10.0),
                2, 2);
        List<Position2D> position2DS = new GridPointGenerator().generate(gridDefinition);

        assertEquals(4, position2DS.size());
    }

    // This overlaps with the full row-major-order test. Remove it after the
    // row-major test asserts the full expected list including both corners.
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

    // Use a 3 x 3 grid here. A 2 x 2 grid has only corner points:
    // (0,0), (10,0), (0,10), (10,10), so it cannot contain (5,5).
    // Prefer asserting the full expected list for row-major order.
    @Test
    public void generatesPointsInRowMajorOrder() {
        RectangularGridDefinition gridDefinition = new RectangularGridDefinition(
                new Position2D(0.0, 0.0),
                new Position2D(10.0, 10.0),
                2, 2);
        List<Position2D> position2DS = new GridPointGenerator().generate(gridDefinition);

        assertEquals(new Position2D(0.0, 0.0), position2DS.getFirst());
        assertEquals(new Position2D(10.0, 10.0), position2DS.get(2));
    }

    // This is a 3 x 2 grid. The expected row-major points are:
    // (0,0), (5,0), (10,0), (0,10), (5,10), (10,10).
    // Index 5 is therefore (10,10), not (5,5).
    @Test
    public void generatesNonSquareGridPoints() {
        RectangularGridDefinition gridDefinition = new RectangularGridDefinition(
                new Position2D(0.0, 0.0),
                new Position2D(10.0, 10.0),
                3, 2);
        List<Position2D> position2DS = new GridPointGenerator().generate(gridDefinition);

        assertEquals(new Position2D(0.0, 0.0), position2DS.getFirst());
        assertEquals(new Position2D(5.0, 5.0), position2DS.get(5));
        assertEquals(new Position2D(10.0, 10.0), position2DS.getLast());
    }

    // Remove this test from GridPointGeneratorTest. Invalid sample counts are
    // already the responsibility of RectangularGridDefinitionTest.
    // Also, GridPointGenerator().generate(...) is missing 'new'.
    @Test
    public void generatesMinimumTwoByTwoGrid() {
        RectangularGridDefinition gridDefinition = new RectangularGridDefinition(
                new Position2D(0.0, 0.0),
                new Position2D(10.0, 10.0),
                1, 1);
        assertThrows(IllegalArgumentException.class, () ->
                GridPointGenerator().generate(gridDefinition));
    }

    // This overlaps with row-major-order testing. If kept, index 5 in a
    // 3 x 3 grid is (10,5), so x is 10.0 and y is 5.0.
    @Test
    public void usesDerivedSpacingBetweenBounds() {
        RectangularGridDefinition gridDefinition = new RectangularGridDefinition(
                new Position2D(0.0, 0.0),
                new Position2D(10.0, 10.0),
                3, 3);
        List<Position2D> position2DS = new GridPointGenerator().generate(gridDefinition);
        double xStep = (10.0 - 0.0) / (3 - 1);
        double yStep = (10.0 - 0.0) / (3 - 1);

        assertEquals(xStep, position2DS.get(5).xMeters());
        assertEquals(yStep, position2DS.get(5).yMeters());
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
}
