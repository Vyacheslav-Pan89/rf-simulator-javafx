# Vector2D

`Vector2D` is an immutable domain value representing a two-dimensional
displacement or direction.

## Components

`Vector2D` stores:

- `deltaXMeters`: the horizontal component in meters.
- `deltaYMeters`: the vertical component in meters.

Both components must be finite. Positive, zero, and negative components are
valid. Constructing a `Vector2D` with `NaN`, positive infinity, or negative
infinity in either component throws `IllegalArgumentException`.

Negative zero is accepted and stored as canonical positive zero for each
component.

## Equality

`Vector2D` uses the value-based equality supplied by a Java record. Two
instances containing the same canonical components are equal.

## Zero Vector

A vector with both components equal to zero is valid and represents no
displacement. Its magnitude is zero.

## Magnitude

`magnitude()` returns the non-negative Euclidean length of the vector:

```text
magnitudeMeters = sqrt(deltaXMeters^2 + deltaYMeters^2)
```

The result is returned as a `Distance`.

## Current Scope

`Vector2D` stores and validates components and supports magnitude calculation.
Normalization, rotation, angles, dot products, projections, and arithmetic
operations should be introduced only when an active milestone requires them.
