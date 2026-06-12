package com.rfsimulator.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FrequencyTest {

    @Test
    public void storesHertz() {
        Frequency frequency = new Frequency(400.0);
        assertEquals(400.0, frequency.hertz());
    }

    @Test
    public void rejectsZero() {
        assertThrows(IllegalArgumentException.class, () ->
                new Frequency(0.0));
    }

    @Test
    public void rejectsNegativeZero() {
        assertThrows(IllegalArgumentException.class, () ->
                new Frequency(-0.0));
    }

    @Test
    public void rejectsNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                new Frequency(-400.0));
    }

    @Test
    public void rejectsNaN() {
        assertThrows(IllegalArgumentException.class, () ->
                new Frequency(Double.NaN));
    }

    @Test
    public void rejectsPositiveInfinity() {
        assertThrows(IllegalArgumentException.class, () ->
                new Frequency(Double.POSITIVE_INFINITY));
    }

    @Test
    public void rejectsNegativeInfinity() {
        assertThrows(IllegalArgumentException.class, () ->
                new Frequency(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void equalHertzValuesAreEqual() {
        assertEquals(new Frequency(400.0), new Frequency(400.0));
    }
}
