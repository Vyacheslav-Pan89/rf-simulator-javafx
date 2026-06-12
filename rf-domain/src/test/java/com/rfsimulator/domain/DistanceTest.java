package com.rfsimulator.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class DistanceTest {

    @Test
    public void storesMeters() {
        Distance distance = new Distance(100.0);
        assertEquals(100, distance.meters());
    }

    @Test
    public void acceptsZero() {
        Distance distance = new Distance(0.0);
        assertEquals(0, distance.meters());
    }

    @Test
    public void rejectsNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                new Distance(-1));

    }

    @Test
    public void rejectsNaN() {
        assertThrows(IllegalArgumentException.class, () ->
                new Distance(Double.NaN)
        );
    }

    @Test
    public void rejectsPositiveInfinity() {
        assertThrows(IllegalArgumentException.class, () ->
                new Distance(Double.POSITIVE_INFINITY)
        );
    }

    @Test
    public void rejectsNegativeInfinity() {
        assertThrows(IllegalArgumentException.class, () ->
                new Distance(Double.NEGATIVE_INFINITY)
        );
    }

    @Test
    public void canonicalizesNegativeZero() {
        Distance distance = new Distance(-0.0);

        assertEquals(new Distance(0.0), distance);
        assertEquals(0.0, distance.meters());
    }

}
