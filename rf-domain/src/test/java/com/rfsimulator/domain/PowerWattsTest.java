package com.rfsimulator.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PowerWattsTest {

    @Test
    public void storesWatts() {
        PowerWatts powerWatts = new PowerWatts(10.0);
        assertEquals(10.0, powerWatts.watts());
    }

    @Test
    public void acceptsZero() {
        PowerWatts powerWatts = new PowerWatts(0.0);
        assertEquals(0.0, powerWatts.watts());
    }

    @Test
    public void canonicalizesNegativeZero() {
        PowerWatts powerWatts = new PowerWatts(-0.0);

        assertEquals(new PowerWatts(0.0), powerWatts);
        assertEquals(0.0, powerWatts.watts());
    }

    @Test
    public void rejectsNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                new PowerWatts(-10.0));
    }

    @Test
    public void rejectsNaN() {
        assertThrows(IllegalArgumentException.class, () ->
                new PowerWatts(Double.NaN));
    }

    @Test
    public void rejectsPositiveInfinity() {
        assertThrows(IllegalArgumentException.class, () ->
                new PowerWatts(Double.POSITIVE_INFINITY));
    }

    @Test
    public void rejectsNegativeInfinity() {
        assertThrows(IllegalArgumentException.class, () ->
                new PowerWatts(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void equalWattValuesAreEqual() {
        assertEquals(new PowerWatts(400.0), new PowerWatts(400.0));
    }

}
