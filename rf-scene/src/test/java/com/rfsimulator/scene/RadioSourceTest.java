package com.rfsimulator.scene;

import com.rfsimulator.domain.Frequency;
import com.rfsimulator.domain.Position2D;
import com.rfsimulator.domain.PowerWatts;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RadioSourceTest {

    private final Position2D testPosition = new Position2D(5, 5);
    private final Frequency testFrequency = new Frequency(100);
    private final PowerWatts testTransmitPower = new PowerWatts(10);

    @Test
    public void testComponentStorage() {
        RadioSource radioSource =
                new RadioSource(
                        testPosition,
                        testTransmitPower,
                        testFrequency);

        assertEquals(testPosition, radioSource.position());
        assertEquals(testTransmitPower, radioSource.transmitPower());
        assertEquals(testFrequency, radioSource.frequency());
    }

    @Test
    public void testThrowsNullPointerExceptionWhenPositionIsNull() {
        assertThrows(NullPointerException.class, () ->
                new RadioSource(
                        null,
                        testTransmitPower,
                        testFrequency));
    }

    @Test
    public void testThrowsNullPointerExceptionWhenTransmitPowerIsNull() {
        assertThrows(NullPointerException.class, () ->
                new RadioSource(
                        testPosition,
                        null,
                        testFrequency));
    }

    @Test
    public void testThrowsNullPointerExceptionWhenFrequencyIsNull() {
        assertThrows(NullPointerException.class, () ->
                new RadioSource(
                        testPosition,
                        testTransmitPower,
                        null));
    }
}
