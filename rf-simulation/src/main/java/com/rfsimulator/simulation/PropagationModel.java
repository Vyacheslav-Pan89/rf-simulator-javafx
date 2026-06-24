package com.rfsimulator.simulation;

import com.rfsimulator.domain.Position2D;
import com.rfsimulator.domain.PowerDbm;
import com.rfsimulator.scene.RadioSource;

public interface PropagationModel {

    PowerDbm calculatePower(RadioSource radioSource, Position2D position);
}

