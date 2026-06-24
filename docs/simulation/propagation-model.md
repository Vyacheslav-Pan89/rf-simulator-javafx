# PropagationModel

`PropagationModel` is the first planned simulation contract for calculating a
documented educational propagation result at one sample position.

## Module And Package

`PropagationModel` belongs in the future `rf-simulation` module under the
`com.rfsimulator.simulation` package.

The simulation module may depend on `rf-domain` and `rf-scene`. It must not
depend on JavaFX, application state, rendering code, or persistence.

## Input

The planned contract accepts:

- a `RadioSource` describing the transmitter inputs;
- a `Position2D` sample position where the result is calculated.

Passing `null` for either input should throw `NullPointerException`.

## Output

The first planned output is received power at the sample position, represented
as `PowerDbm`.

The output unit is dBm because received RF power is commonly inspected on a
logarithmic scale. Any conversion from linear transmit power must be explicit
inside the model implementation and documented by that model.

## Source Boundary Behavior

The propagation contract must define behavior at or near the source before any
equation is implemented.

For the first model, a sample position exactly equal to the source position
should be treated as outside the valid calculation range. The model should
throw `IllegalArgumentException` rather than return an infinite, undefined, or
misleading value.

Near-source minimum-distance behavior may be added later only when a specific
model documents the chosen rule and its educational limitation.

## Responsibilities

The contract defines input and output meanings only. It does not:

- choose a grid;
- generate sample points;
- store simulation result snapshots;
- combine multiple sources;
- normalize display values;
- render results.

Each propagation model implementation must document its equation, units,
assumptions, valid range, boundary behavior, limitations, and reference cases
before it is considered complete.

## Current Scope

This milestone should define the contract shape without implementing a
propagation equation. Free-space path loss, grid sampling, multiple sources,
obstacles, terrain, antennas, and visualization belong to later milestones.
