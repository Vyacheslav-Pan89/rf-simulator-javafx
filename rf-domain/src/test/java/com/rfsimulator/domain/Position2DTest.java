package com.rfsimulator.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Position2DTest {
    @Test
    public void storesCoordinates() {
        Position2D position2D = new Position2D(1.0, 2.0);
        assertEquals(1.0, position2D.xMeters());
        assertEquals(2.0, position2D.yMeters());
    }

    @Test
    public void acceptsZeroCoordinates() {
        Position2D position2D = new Position2D(0.0, 0.0);

        assertEquals(0.0, position2D.xMeters());
        assertEquals(0.0, position2D.yMeters());
    }

    @Test
    public void canonicalizesNegativeZeroCoordinates() {
        Position2D position2D = new Position2D(-0.0, -0.0);

        assertEquals(new Position2D(0.0, 0.0), position2D);
        assertEquals(0.0, position2D.xMeters());
        assertEquals(0.0, position2D.yMeters());
    }

    @Test
    public void rejectsNaNCoordinates() {

        assertThrows(IllegalArgumentException.class, () ->
                new Position2D(Double.NaN, -0.0));
        assertThrows(IllegalArgumentException.class, () ->
                new Position2D(-0.0, Double.NaN));

    }

    @Test
    public void rejectsPositiveInfinityCoordinates() {

        assertThrows(IllegalArgumentException.class, () ->
                new Position2D(Double.POSITIVE_INFINITY, -0.0));
        assertThrows(IllegalArgumentException.class, () ->
                new Position2D(-0.0, Double.POSITIVE_INFINITY));

    }

    @Test
    public void rejectsNegativeInfinityCoordinates() {

        assertThrows(IllegalArgumentException.class, () ->
                new Position2D(Double.NEGATIVE_INFINITY, -0.0));
        assertThrows(IllegalArgumentException.class, () ->
                new Position2D(-0.0, Double.NEGATIVE_INFINITY));
    }

    @Test
    public void equalCoordinatesAreEqual() {
        assertEquals(new Position2D(1.0, 2.0), new Position2D(1.0, 2.0));
    }

    @Test
    public void acceptsNegativeCoordinates() {
        Position2D position2D = new Position2D(-1.0, -2.0);

        assertEquals(-1.0, position2D.xMeters());
        assertEquals(-2.0, position2D.yMeters());
    }
}
