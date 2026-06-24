package com.rfsimulator.simulation;

import com.rfsimulator.domain.Position2D;
import com.rfsimulator.domain.PowerDbm;
import com.rfsimulator.scene.RadioSource;

import java.util.Objects;

public class FreeSpacePathLossModel implements PropagationModel {
    @Override
    public PowerDbm calculatePower(RadioSource radioSource, Position2D position) {
        Objects.requireNonNull(radioSource, "radioSource");
        Objects.requireNonNull(position, "position");

        if (radioSource.position().equals(position)) {
            throw new IllegalArgumentException("Radio source cannot be at the same position as the receiver");
        }

        return null;
    }
}
