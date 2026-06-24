package com.rfsimulator.simulation;

import com.rfsimulator.domain.Position2D;
import com.rfsimulator.scene.RadioSource;

import java.util.List;

public record FieldSampler(RadioSource radioSource, PropagationModel propagationModel, List<Position2D> positions) {
}
