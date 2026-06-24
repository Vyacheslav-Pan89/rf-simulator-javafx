package com.rfsimulator.simulation;

import com.rfsimulator.domain.Frequency;
import com.rfsimulator.domain.Position2D;
import com.rfsimulator.domain.PowerWatts;
import com.rfsimulator.scene.RadioSource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FieldSamplerTest {

    private final RadioSource testRadioSource = new RadioSource(
            new Position2D(0.0, 0.0),
            new PowerWatts(1.0),
            new Frequency(100000000.0));

    private final PropagationModel testPathLossModel = new FreeSpacePathLossModel();

    private final List<Position2D> testPositions = List.of(
            new Position2D(1000.0, 0.0),
            new Position2D(0.0, 1000.0),
            new Position2D(2000.0, 0.0));

    @Test
    public void nullRadioSourceThrowsException() {
        assertThrows(NullPointerException.class, () ->
                new FieldSampler(
                        null,
                        testPathLossModel,
                        testPositions));
    }

    @Test
    public void nullPropagationModelThrowsException() {
        assertThrows(NullPointerException.class, () ->
                new FieldSampler(
                        testRadioSource,
                        null,
                        testPositions));
    }

    @Test
    public void nullPositionListThrowsException() {
        assertThrows(NullPointerException.class, () ->
                new FieldSampler(
                        testRadioSource,
                        testPathLossModel,
                        null));
    }

    @Test
    public void invalidPositionListThrowsException() {
        List<Position2D> invalidList = List.of(
                new Position2D(0.0, 0.0),
                null,
                new Position2D(1000.0, 0.0));
        assertThrows(NullPointerException.class, () ->
                new FieldSampler(
                        testRadioSource,
                        testPathLossModel,
                        invalidList));
    }

    @Test
    public void emptySampleCollectionReturnsEmptyResult() {
        List<Position2D> emptyPositions = List.of();
        FieldSampler fieldSampler = new FieldSampler(
                testRadioSource,
                testPathLossModel,
                emptyPositions);
        assertEquals(0, fieldSampler.calculate().size());
    }

    @Test
    public void sampledResultCountMatchesInputSampleCount() {
        FieldSampler fieldSampler = new FieldSampler(
                testRadioSource,
                testPathLossModel,
                testPositions);
        assertEquals(3, fieldSampler.calculate().size());
    }

}
