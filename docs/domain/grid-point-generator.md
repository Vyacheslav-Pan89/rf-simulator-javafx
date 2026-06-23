# GridPointGenerator

`GridPointGenerator` creates deterministic sample positions from a valid
`RectangularGridDefinition`.

## Input

The generator accepts a `RectangularGridDefinition`.

Passing `null` throws `NullPointerException`.

The definition owns all validation for bounds and sample counts. The generator
does not reinterpret invalid grid definitions.

## Output

The generator returns an ordered list of `Position2D` sample points.

The returned list must not be mutable by callers.

## Boundary Inclusion

Both grid boundaries are included.

The first generated point is the minimum corner:

```text
(minimum.xMeters, minimum.yMeters)
```

The last generated point is the maximum corner:

```text
(maximum.xMeters, maximum.yMeters)
```

## Spacing

Spacing is derived from bounds and sample counts:

```text
xStep = (maximum.xMeters - minimum.xMeters) / (xSamples - 1)
yStep = (maximum.yMeters - minimum.yMeters) / (ySamples - 1)
```

Spacing is not stored in `RectangularGridDefinition`.

Floating-point calculations may produce small representation differences from
the mathematical result. Tests that compare generated coordinates should use a
deliberately chosen tolerance when exact equality is not appropriate.

## Ordering

Points are generated in row-major order:

1. Start at `minimum.yMeters`.
2. For each row, generate x coordinates from `minimum.xMeters` to
   `maximum.xMeters`.
3. Move to the next y coordinate.
4. Finish at `maximum.yMeters`.

For a grid with:

```text
minimum = (0, 0)
maximum = (10, 10)
xSamples = 3
ySamples = 3
```

the generated order is:

```text
(0, 0), (5, 0), (10, 0),
(0, 5), (5, 5), (10, 5),
(0, 10), (5, 10), (10, 10)
```

## Sample Count

The number of generated points is:

```text
totalSamples = xSamples * ySamples
```

## Current Scope

`GridPointGenerator` only generates positions. It does not calculate RF
values, store simulation results, render points, or decide visualization
behavior.
