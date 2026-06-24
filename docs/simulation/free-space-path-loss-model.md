# FreeSpacePathLossModel

`FreeSpacePathLossModel` is the first planned educational propagation model.
It calculates received power at one two-dimensional sample position using the
free-space path loss equation.

## Module And Package

`FreeSpacePathLossModel` belongs in the `rf-simulation` module under the
`com.rfsimulator.simulation` package.

The model implements `PropagationModel`. It may depend on `rf-domain` and
`rf-scene`. It must not depend on JavaFX, application state, rendering code,
grid sampling, visualization, or persistence.

## Inputs

The model accepts:

- a `RadioSource` describing source position, transmit power, and frequency;
- a `Position2D` sample position where received power is calculated.

Passing `null` for either input should throw `NullPointerException`.

The source provides:

- `position`: transmitter position in meters;
- `transmitPower`: transmit power as `PowerWatts`;
- `frequency`: radio frequency in hertz.

The sample position provides:

- coordinates in meters in the same two-dimensional coordinate system as the
  source.

## Output

The model returns received power as `PowerDbm`.

Transmit power is stored as `PowerWatts` by `RadioSource`. The model converts
that value explicitly to `PowerDbm` before applying path loss so linear and
logarithmic quantities are not mixed silently.

## Equation

The model uses:

```text
Pr(dBm) = Pt(dBm) - FSPL(dB)
```

where:

```text
FSPL(dB) = 20 log10(d_km) + 20 log10(f_MHz) + 32.44
```

Terms:

- `Pr(dBm)`: received power at the sample position.
- `Pt(dBm)`: transmit power converted from `PowerWatts`.
- `d_km`: distance from source to sample in kilometers.
- `f_MHz`: source frequency in megahertz.
- `32.44`: constant for distance in kilometers and frequency in megahertz.

Distance is calculated from the source position to the sample position using
the existing two-dimensional geometry values. The distance is converted from
meters to kilometers before it is used in the equation. Frequency is converted
from hertz to megahertz before it is used in the equation.

## Boundary Behavior

Passing a sample position exactly equal to the source position should throw
`IllegalArgumentException`.

At zero distance, the free-space path loss equation would require
`log10(0)`, which is undefined and would produce a misleading result. This
model does not introduce a near-source clamp or minimum-distance workaround.
Any such rule should be added only in a later model or milestone with explicit
documentation of its educational limitation.

Samples at any positive finite distance are valid if the source components are
valid.

## Assumptions

This model assumes:

- one point-like transmitter;
- one sample position;
- unobstructed free-space propagation;
- no antenna gain or antenna pattern;
- no receiver antenna gain;
- no polarization mismatch;
- no reflection, diffraction, scattering, terrain, buildings, atmosphere, or
  multipath;
- no cable loss, connector loss, or transmitter hardware limits;
- distance is the two-dimensional straight-line distance between source and
  sample.

## Valid Range

The model is intended for educational positive-distance calculations only.

Valid inputs require:

- non-null source;
- non-null sample position;
- positive source frequency, enforced by `Frequency`;
- finite, non-negative transmit power, enforced by `PowerWatts`;
- finite source and sample coordinates, enforced by `Position2D`;
- sample position different from the source position.

If transmit power is zero watts, converting to finite dBm is impossible. The
model should allow the existing `PowerWatts.toDbm()` behavior to reject that
conversion rather than inventing a received-power value.

## Expected Behavior

For a fixed source power and frequency:

- received power decreases as distance increases;
- received power decreases as frequency increases;
- identical inputs produce identical outputs.

For a fixed distance and frequency, increasing transmit power increases
received power by the same number of decibels as the transmit-power increase.

## Reference Cases

These cases use the equation above and should be used as implementation tests.

### 1 W At 1 km And 100 MHz

Inputs:

- transmit power: `1.0 W`
- source position: `(0.0 m, 0.0 m)`
- sample position: `(1000.0 m, 0.0 m)`
- frequency: `100,000,000 Hz`

Calculation:

```text
Pt = 30.0 dBm
d = 1.0 km
f = 100.0 MHz
FSPL = 20 log10(1.0) + 20 log10(100.0) + 32.44
FSPL = 72.44 dB
Pr = 30.0 - 72.44
Pr = -42.44 dBm
```

Expected result:

```text
-42.44 dBm
```

### 10 W At 1 km And 100 MHz

Inputs:

- transmit power: `10.0 W`
- source position: `(0.0 m, 0.0 m)`
- sample position: `(1000.0 m, 0.0 m)`
- frequency: `100,000,000 Hz`

Calculation:

```text
Pt = 40.0 dBm
FSPL = 72.44 dB
Pr = 40.0 - 72.44
Pr = -32.44 dBm
```

Expected result:

```text
-32.44 dBm
```

### 1 W At 2 km And 100 MHz

Inputs:

- transmit power: `1.0 W`
- source position: `(0.0 m, 0.0 m)`
- sample position: `(2000.0 m, 0.0 m)`
- frequency: `100,000,000 Hz`

Calculation:

```text
Pt = 30.0 dBm
d = 2.0 km
f = 100.0 MHz
FSPL = 20 log10(2.0) + 20 log10(100.0) + 32.44
FSPL = 78.46059991327962 dB
Pr = 30.0 - 78.46059991327962
Pr = -48.46059991327962 dBm
```

Expected result:

```text
-48.46059991327962 dBm
```

## Responsibilities

This model calculates received power for one source and one sample position.
It does not:

- generate grid sample points;
- store simulation result snapshots;
- combine multiple sources;
- choose display ranges;
- normalize values for rendering;
- draw heatmaps, markers, labels, or legends;
- model obstacles, terrain, antenna patterns, or multiple paths.

## Limitations

This is an educational model, not a professional RF planning tool. Its output
should not be used for real installation design, certification, navigation,
safety analysis, or regulatory decisions.

The model deliberately omits many real-world effects so the first propagation
calculation remains understandable, deterministic, and testable.
