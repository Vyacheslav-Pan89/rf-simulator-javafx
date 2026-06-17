package com.rfsimulator.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RectangularGridDefinitionTest {

    @Test
    public void storesBoundsAndSampleCounts() {
        RectangularGridDefinition rectangularGridDefinition =
                new RectangularGridDefinition(
                        new Position2D(1.0, 1.0),
                        new Position2D(5.0, 5.0),
                        2,
                        2);

        assertEquals(1.0, rectangularGridDefinition.minimum().xMeters());
        assertEquals(1.0, rectangularGridDefinition.minimum().yMeters());
        assertEquals(5.0, rectangularGridDefinition.maximum().xMeters());
        assertEquals(5.0, rectangularGridDefinition.maximum().yMeters());
        assertEquals(2, rectangularGridDefinition.xSamples());
        assertEquals(2, rectangularGridDefinition.ySamples());
    }

    @Test
    public void acceptsNonSquareSampleCounts() {
        RectangularGridDefinition rectangularGridDefinition =
                new RectangularGridDefinition(
                        new Position2D(1.0, 1.0),
                        new Position2D(5.0, 5.0),
                        4,
                        6);

        assertEquals(1.0, rectangularGridDefinition.minimum().xMeters());
        assertEquals(1.0, rectangularGridDefinition.minimum().yMeters());
        assertEquals(5.0, rectangularGridDefinition.maximum().xMeters());
        assertEquals(5.0, rectangularGridDefinition.maximum().yMeters());
        assertEquals(4, rectangularGridDefinition.xSamples());
        assertEquals(6, rectangularGridDefinition.ySamples());
    }

    @Test
    public void rejectsNullMinimum() {
        assertThrows(NullPointerException.class, () ->
                new RectangularGridDefinition(
                        null,
                        new Position2D(5.0, 5.0),
                        4,
                        6));
    }

    @Test
    public void rejectsNullMaximum() {
        assertThrows(NullPointerException.class, () ->
                new RectangularGridDefinition(
                        new Position2D(1.0, 1.0),
                        null,
                        4,
                        6));
    }

    @Test
    public void rejectsReversedXBounds() {
        assertThrows(IllegalArgumentException.class, () ->
                new RectangularGridDefinition(
                        new Position2D(5.0, 1.0),
                        new Position2D(1.0, 5.0),
                        4,
                        6));
    }

    @Test
    public void rejectsReversedYBounds() {
        assertThrows(IllegalArgumentException.class, () ->
                new RectangularGridDefinition(
                        new Position2D(1.0, 5.0),
                        new Position2D(5.0, 1.0),
                        4,
                        6));
    }

    @Test
    public void rejectsEqualXBounds() {
        assertThrows(IllegalArgumentException.class, () ->
                new RectangularGridDefinition(
                        new Position2D(1.0, 1.0),
                        new Position2D(1.0, 5.0),
                        4,
                        6));
    }

    @Test
    public void rejectsEqualYBounds() {
        assertThrows(IllegalArgumentException.class, () ->
                new RectangularGridDefinition(
                        new Position2D(1.0, 1.0),
                        new Position2D(5.0, 1.0),
                        4,
                        6));
    }

    @Test
    public void rejectsTooFewXSamples() {
        assertThrows(IllegalArgumentException.class, () ->
                new RectangularGridDefinition(
                        new Position2D(1.0, 1.0),
                        new Position2D(5.0, 5.0),
                        1,
                        6));
    }

    @Test
    public void rejectsTooFewYSamples() {
        assertThrows(IllegalArgumentException.class, () ->
                new RectangularGridDefinition(
                        new Position2D(1.0, 1.0),
                        new Position2D(5.0, 5.0),
                        2,
                        1));
    }

    @Test
    public void equalDefinitionsAreEqual() {
        RectangularGridDefinition rectangularGridDefinition1 = new RectangularGridDefinition(
                new Position2D(1.0, 1.0),
                new Position2D(5.0, 5.0),
                2,
                2);
        RectangularGridDefinition rectangularGridDefinition2 = new RectangularGridDefinition(
                new Position2D(1.0, 1.0),
                new Position2D(5.0, 5.0),
                2,
                2);
        assertEquals(rectangularGridDefinition1, rectangularGridDefinition2);
    }

    @Test
    public void acceptsOddSampleCounts() {
        RectangularGridDefinition rectangularGridDefinition =
                new RectangularGridDefinition(
                        new Position2D(1.0, 1.0),
                        new Position2D(5.0, 5.0),
                        5,
                        5);

        assertEquals(5, rectangularGridDefinition.xSamples());
        assertEquals(5, rectangularGridDefinition.ySamples());
    }
}
