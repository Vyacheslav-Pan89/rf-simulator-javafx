package com.rfsimulator.simulation;

import com.rfsimulator.domain.Frequency;
import com.rfsimulator.domain.Position2D;
import com.rfsimulator.domain.PowerWatts;
import com.rfsimulator.scene.RadioSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FreeSpacePathLossModelTest {

    @Test
    public void testEqualPositionsThrowsException() {
        RadioSource radioSource =
                new RadioSource(
                        new Position2D(5, 5),
                        new PowerWatts(100),
                        new Frequency(1000));

        assertThrows(IllegalArgumentException.class, () ->
                new FreeSpacePathLossModel()
                        .calculatePower(radioSource, new Position2D(5, 5)));
    }

    @Test
    public void testThrowsNullPointerExceptionWhenNullRadioSource() {
        assertThrows(NullPointerException.class, () ->
                new FreeSpacePathLossModel().calculatePower(null, new Position2D(5, 5)));
    }

    @Test
    public void testThrowsNullPointerExceptionWhenNullTargetPosition() {
        RadioSource radioSource =
                new RadioSource(
                        new Position2D(5, 5),
                        new PowerWatts(100),
                        new Frequency(1000));

        assertThrows(NullPointerException.class, () ->
                new FreeSpacePathLossModel().calculatePower(radioSource, null));
    }
}
