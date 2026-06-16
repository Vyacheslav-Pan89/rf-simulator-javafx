package com.rfsimulator.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Vector2DTest {

    @Test
    public void storesComponents() {
        Vector2D vector2D = new Vector2D(1.0, 2.0);

        assertEquals(1.0, vector2D.deltaXMeters());
        assertEquals(2.0, vector2D.deltaYMeters());
    }

    @Test
    public void acceptsZeroComponents() {
        Vector2D vector2D = new Vector2D(0.0, 0.0);

        assertEquals(0.0, vector2D.deltaXMeters());
        assertEquals(0.0, vector2D.deltaYMeters());
    }

    @Test
    public void acceptsNegativeComponents() {
        Vector2D vector2D = new Vector2D(-1.0, -1.0);

        assertEquals(-1.0, vector2D.deltaXMeters());
        assertEquals(-1.0, vector2D.deltaYMeters());
    }

    @Test
    public void canonicalizesNegativeZeroComponents() {
        Vector2D vector2D = new Vector2D(-0.0, -0.0);

        assertEquals(new Vector2D(0.0, 0.0), vector2D);
        assertEquals(0.0, vector2D.deltaXMeters());
        assertEquals(0.0, vector2D.deltaYMeters());
    }

    @Test
    public void rejectsNaNComponents() {
        assertThrows(IllegalArgumentException.class, () ->
                new Vector2D(0.0, Double.NaN));
        assertThrows(IllegalArgumentException.class, () ->
                new Vector2D(Double.NaN, 0.0));
    }

    @Test
    public void rejectsPositiveInfinityComponents() {
        assertThrows(IllegalArgumentException.class, () ->
                new Vector2D(Double.POSITIVE_INFINITY, 0.0));
        assertThrows(IllegalArgumentException.class, () ->
                new Vector2D(0.0, Double.POSITIVE_INFINITY));
    }

    @Test
    public void rejectsNegativeInfinityComponents() {
        assertThrows(IllegalArgumentException.class, () ->
                new Vector2D(0.0, Double.NEGATIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () ->
                new Vector2D(Double.NEGATIVE_INFINITY, 0.0));
    }

    @Test
    public void equalComponentsAreEqual() {
        assertEquals(new Vector2D(1.0, 1.0), new Vector2D(1.0, 1.0));
    }

    @Test
    public void zeroVectorHasZeroMagnitude() {
        Distance magnitude = new Vector2D(0.0, 0.0).magnitude();

        assertEquals(new Distance(0.0), magnitude);
    }

    @Test
    public void calculatesMagnitude() {
        Distance magnitude = new Vector2D(3.0, 4.0).magnitude();

        assertEquals(new Distance(5.0), magnitude);
    }

    @Test
    public void magnitudeUsesAbsoluteComponentValues() {
        Distance magnitude = new Vector2D(-3.0, -4.0).magnitude();

        assertEquals(new Distance(5.0), magnitude);
    }

    @Test
    public void calculatesMagnitudeForLargeComponentsWithoutOverflow() {
        Distance magnitude = new Vector2D(Double.MAX_VALUE, 0.0).magnitude();

        assertEquals(new Distance(Double.MAX_VALUE), magnitude);
    }

}
