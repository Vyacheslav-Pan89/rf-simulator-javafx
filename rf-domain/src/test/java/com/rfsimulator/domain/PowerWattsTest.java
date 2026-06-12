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

    @Test
    public void convertsOneMilliwattToZeroDbm() {
        double dbm = new PowerWatts(0.001).toDbm().dbm();
        assertEquals(0.0, dbm, 1e-12);
    }

    @Test
    public void convertsOneWattToThirtyDbm() {
        double dbm = new PowerWatts(1.0).toDbm().dbm();
        assertEquals(30.0, dbm, 1e-12);
    }

    @Test
    public void convertsTenWattsToFortyDbm() {
        double dbm = new PowerWatts(10).toDbm().dbm();
        assertEquals(40.0, dbm, 1e-12);
    }

    @Test
    public void rejectsConvertingZeroWattsToDbm() {
        assertThrows(IllegalStateException.class, () ->
                new PowerWatts(0.0).toDbm()
        );
    }
}
