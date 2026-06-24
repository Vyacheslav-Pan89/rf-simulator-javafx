package com.rfsimulator.simulation;

import com.rfsimulator.domain.Distance;
import com.rfsimulator.domain.Position2D;
import com.rfsimulator.domain.PowerDbm;
import com.rfsimulator.scene.RadioSource;

import java.util.Objects;

public class FreeSpacePathLossModel implements PropagationModel {

    private static final double METERS_PER_KILOMETER = 1_000.0;
    private static final double HERTZ_PER_MEGAHERTZ = 1_000_000.0;
    private static final double FSPL_CONSTANT_FOR_KM_AND_MHZ = 32.44;

    @Override
    public PowerDbm calculatePower(RadioSource radioSource, Position2D position) {
        Objects.requireNonNull(radioSource, "radioSource");
        Objects.requireNonNull(position, "position");
        if (radioSource.position().equals(position)) {
            throw new IllegalArgumentException("Radio source cannot be at the same position as the receiver");
        }

        Distance distance = radioSource.position().distanceTo(position);
        double freeSpaceLoss = 20 * Math.log10(distance.meters() / METERS_PER_KILOMETER)
                + 20 * Math.log10(radioSource.frequency().hertz() / HERTZ_PER_MEGAHERTZ)
                + FSPL_CONSTANT_FOR_KM_AND_MHZ;

        double powerAtReceiver = radioSource.transmitPower().toDbm().dbm() - freeSpaceLoss;

        return new PowerDbm(powerAtReceiver);
    }
}
