# RectangularGridDefinition

`RectangularGridDefinition` describes a valid rectangular sampling area without
generating sample points.

## Bounds

The grid is defined by two positions:

- `minimum`: the lower-left corner of the grid.
- `maximum`: the upper-right corner of the grid.

Bounds use the documented two-dimensional coordinate system. Coordinates are
measured in meters.

`minimum.xMeters` must be less than `maximum.xMeters`.
`minimum.yMeters` must be less than `maximum.yMeters`.

Reversed bounds and zero-width or zero-height bounds are invalid.

## Boundary Inclusion

Bounds represent sample-point limits, not cells.

Both the minimum and maximum boundaries are included when sample points are
generated in a later milestone.

For example, a grid with x bounds from `0` to `10` and `3` x samples will
later generate x coordinates:

```text
0, 5, 10
```

Point generation is not part of `RectangularGridDefinition`.

## Resolution

Resolution is expressed as sample count, not spacing:

- `xSamples`: number of sample points along the x axis.
- `ySamples`: number of sample points along the y axis.

Both sample counts must be greater than or equal to `2`.

Non-square grids are valid. `xSamples` and `ySamples` may be different.

## Valid Values

Constructing a `RectangularGridDefinition` with invalid values throws
`IllegalArgumentException`.

Invalid values include:

- `null` minimum or maximum positions.
- Reversed x or y bounds.
- Equal x or y bounds.
- `xSamples` less than `2`.
- `ySamples` less than `2`.

Coordinate finiteness is enforced by `Position2D`.

## Equality

`RectangularGridDefinition` uses the value-based equality supplied by a Java
record. Two definitions with the same bounds and sample counts are equal.

## Current Scope

`RectangularGridDefinition` stores and validates grid bounds and sample
counts only. It does not generate points, calculate spacing, render a grid,
or perform RF calculations. Point generation should be introduced in the next
milestone.
