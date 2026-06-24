# RadioSource

`RadioSource` is the first planned scene value representing one educational
radio transmitter in a two-dimensional scenario.

## Module And Package

`RadioSource` belongs in the future `rf-scene` module under the
`com.rfsimulator.scene` package.

The scene module may depend on `rf-domain`. `RadioSource` must not depend on
JavaFX, simulation implementations, rendering code, or mutable application
state.

## Components

The minimal planned source stores:

- `position`: a `Position2D` source location in meters.
- `transmitPower`: a `PowerWatts` linear transmit power.
- `frequency`: a `Frequency` radio frequency in hertz.

All components are required. Passing `null` for any component should throw
`NullPointerException`.

The component value types own their own validation:

- `Position2D` validates finite coordinates.
- `PowerWatts` validates finite, non-negative power.
- `Frequency` validates finite, positive frequency.

## Meaning

`RadioSource` describes source inputs only. It does not calculate propagation,
store sampled results, decide display values, or draw markers.

Transmit power is stored as `PowerWatts` so linear and logarithmic quantities
are not mixed silently. Propagation models may convert to `PowerDbm`
explicitly when a documented equation requires it.

## Current Scope

Only one source representation should exist during this milestone. Additional
source types, labels, colors, enable toggles, antenna patterns, altitude, and
multiple-source behavior should wait for later milestones with explicit
responsibility for those concepts.
