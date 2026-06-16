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

    @Test
    public void calculatesVectorToAnotherPosition() {
        Position2D position2D = new Position2D(1.0, 2.0);
        Vector2D displacement = position2D.vectorTo(new Position2D(3.0, 4.0));

        assertEquals(new Vector2D(2.0, 2.0), displacement);
    }

    @Test
    public void calculatesVectorToSamePosition() {
        Position2D position2D = new Position2D(1.0, 2.0);
        Vector2D displacement = position2D.vectorTo(new Position2D(1.0, 2.0));

        assertEquals(new Vector2D(0.0, 0.0), displacement);
    }

    @Test
    public void rejectsNullVectorTarget() {
        Position2D position2D = new Position2D(1.0, 2.0);
        assertThrows(NullPointerException.class, () ->
                position2D.vectorTo(null));
    }

    @Test
    public void calculatesDistanceToAnotherPosition() {
        Position2D position2D = new Position2D(1.0, 2.0);
        Distance distance = position2D.distanceTo(new Position2D(2.0, 3.0));

        assertEquals(Math.sqrt(2.0), distance.meters(), 1e-12);
    }

    @Test
    public void calculatesDistanceToSamePosition() {
        Position2D position2D = new Position2D(1.0, 2.0);
        Distance distance = position2D.distanceTo(new Position2D(1.0, 2.0));

        assertEquals(0.0, distance.meters());
    }

    @Test
    public void calculatesDistanceWithNegativeCoordinates() {
        Position2D position2D = new Position2D(-1.0, -2.0);
        Distance distance = position2D.distanceTo(new Position2D(-2.0, -3.0));

        assertEquals(Math.sqrt(2.0), distance.meters(), 1e-12);
    }

    @Test
    public void calculatesLargeDistanceWithoutOverflow() {
        assertEquals(new Distance(Double.MAX_VALUE),
                new Position2D(0.0, 0.0)
                        .distanceTo(new Position2D(Double.MAX_VALUE, 0.0)));
    }

    @Test
    public void rejectsNullDistanceTarget() {
        assertThrows(NullPointerException.class, () ->
                new Position2D(0.0, 0.0).distanceTo(null));
    }
}
