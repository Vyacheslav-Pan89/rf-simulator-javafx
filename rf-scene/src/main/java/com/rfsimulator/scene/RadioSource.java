package com.rfsimulator.scene;

import com.rfsimulator.domain.Frequency;
import com.rfsimulator.domain.Position2D;
import com.rfsimulator.domain.PowerWatts;

import java.util.Objects;

public record RadioSource(Position2D position,
                          PowerWatts transmitPower,
                          Frequency frequency) {

    public RadioSource {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(transmitPower, "transmitPower");
        Objects.requireNonNull(frequency, "frequency");
    }
}
