package com.rfsimulator.simulation;

import com.rfsimulator.domain.Frequency;
import com.rfsimulator.domain.Position2D;
import com.rfsimulator.domain.PowerDbm;
import com.rfsimulator.domain.PowerWatts;
import com.rfsimulator.scene.RadioSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FreeSpacePathLossModelTest {

    private final RadioSource testRadioSource =
            new RadioSource(
                    new Position2D(0.0, 0.0),
                    new PowerWatts(1.0),
                    new Frequency(100000000.0));

    private final Position2D testTargetPosition = new Position2D(1000.0, 0.0);

    @Test
    public void testEqualPositionsThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FreeSpacePathLossModel()
                        .calculatePower(testRadioSource, testRadioSource.position()));
    }

    @Test
    public void testThrowsNullPointerExceptionWhenNullRadioSource() {
        assertThrows(NullPointerException.class, () ->
                new FreeSpacePathLossModel().calculatePower(null, testTargetPosition));
    }

    @Test
    public void testThrowsNullPointerExceptionWhenNullTargetPosition() {
        assertThrows(NullPointerException.class, () ->
                new FreeSpacePathLossModel().calculatePower(testRadioSource, null));
    }

    @Test
    public void calculatesReceivedPowerForReferenceCase1() {
        FreeSpacePathLossModel freeSpacePathLossModel = new FreeSpacePathLossModel();
        PowerDbm powerDbm = freeSpacePathLossModel.calculatePower(testRadioSource, testTargetPosition);
        assertEquals(-42.44, powerDbm.dbm(), 1e-12);
    }

    @Test
    public void calculatesReceivedPowerForReferenceCase2() {
        FreeSpacePathLossModel freeSpacePathLossModel = new FreeSpacePathLossModel();
        PowerDbm powerDbm = freeSpacePathLossModel
                .calculatePower(new RadioSource(
                                new Position2D(0.0, 0.0),
                                new PowerWatts(10.0),
                                new Frequency(100_000_000.0)),
                        new Position2D(0.0, 1000.0));

        assertEquals(-32.44, powerDbm.dbm(), 1e-12);
    }

    @Test
    public void calculatesReceivedPowerForReferenceCase3() {
        FreeSpacePathLossModel freeSpacePathLossModel = new FreeSpacePathLossModel();
        PowerDbm powerDbm = freeSpacePathLossModel
                .calculatePower(new RadioSource(
                                new Position2D(0.0, 0.0),
                                new PowerWatts(1.0),
                                new Frequency(100_000_000.0)),
                        new Position2D(2000.0, 0.0));
        assertEquals(-48.46059991327962, powerDbm.dbm(), 1e-12);
    }

    @Test
    public void zeroSourcePowerThrowsException() {
        assertThrows(IllegalStateException.class, () ->
                new FreeSpacePathLossModel().calculatePower(new RadioSource(
                                new Position2D(0.0, 0.0),
                                new PowerWatts(0.0),
                                new Frequency(100_000_000.0)),
                        testTargetPosition));
    }
}
