package com.rfsimulator.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PowerDbmTest {

    @Test
    public void storesDbm() {
        PowerDbm powerDbm = new PowerDbm(3.0);
        assertEquals(3.0, powerDbm.dbm());
    }

    @Test
    public void acceptsZero() {
        PowerDbm powerDbm = new PowerDbm(0.0);
        assertEquals(0.0, powerDbm.dbm());
    }

    @Test
    public void acceptsNegative() {
        PowerDbm powerDbm = new PowerDbm(-3.0);
        assertEquals(-3.0, powerDbm.dbm());
    }

    @Test
    public void canonicalizesNegativeZero() {
        PowerDbm powerDbm = new PowerDbm(-0.0);

        assertEquals(new PowerDbm(0.0), powerDbm);
        assertEquals(0.0, powerDbm.dbm());
    }

    @Test
    public void rejectsNaN() {
        assertThrows(IllegalArgumentException.class, () ->
                new PowerDbm(Double.NaN));
    }

    @Test
    public void rejectsPositiveInfinity() {
        assertThrows(IllegalArgumentException.class, () ->
                new PowerDbm(Double.POSITIVE_INFINITY));
    }

    @Test
    public void rejectsNegativeInfinity() {
        assertThrows(IllegalArgumentException.class, () ->
                new PowerDbm(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void equalDbmValuesAreEqual() {
        assertEquals(new PowerDbm(3.0), new PowerDbm(3.0));
    }

    @Test
    public void convertsZeroDbmToOneMilliwatt() {
        double watts = new PowerDbm(0.0).toWatts().watts();
        assertEquals(0.001, watts, 1e-12);
    }

    @Test
    public void convertsThirtyDbmToOneWatt() {
        double watts = new PowerDbm(30.0).toWatts().watts();
        assertEquals(1.0, watts, 1e-12);
    }

    @Test
    public void convertsFortyDbmToTenWatts() {
        double watts = new PowerDbm(40.0).toWatts().watts();
        assertEquals(10.0, watts, 1e-12);
    }

    @Test
    public void rejectsConversionWhenWattsOverflow() {
        assertThrows(
                IllegalStateException.class,
                () -> new PowerDbm(Double.MAX_VALUE).toWatts()
        );
    }

    @Test
    public void convertsExtremelyNegativeDbmToZeroWatts() {
        PowerWatts watts = new PowerDbm(-Double.MAX_VALUE).toWatts();

        assertEquals(new PowerWatts(0.0), watts);
    }
}
