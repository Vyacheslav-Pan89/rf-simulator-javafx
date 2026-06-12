# Position2D

`Position2D` is an immutable domain value representing a location in the
documented two-dimensional coordinate system.

## Components

`Position2D` stores:

- `xMeters`: the horizontal coordinate in meters.
- `yMeters`: the vertical coordinate in meters.

Both coordinates must be finite. Positive, zero, and negative coordinates are
valid. Constructing a `Position2D` with `NaN`, positive infinity, or negative
infinity in either coordinate throws `IllegalArgumentException`.

Negative zero is accepted and stored as canonical positive zero for each
coordinate.

## Equality

`Position2D` uses the value-based equality supplied by a Java record. Two
instances containing the same canonical coordinates are equal.

## Displacement To Another Position

`vectorTo(Position2D other)` returns the displacement from this position to
`other`:

```text
deltaXMeters = other.xMeters - this.xMeters
deltaYMeters = other.yMeters - this.yMeters
```

Passing `null` throws `NullPointerException`.

## Distance To Another Position

`distanceTo(Position2D other)` returns the non-negative Euclidean distance
from this position to `other`:

```text
distanceMeters = sqrt(
    (other.xMeters - this.xMeters)^2
    + (other.yMeters - this.yMeters)^2
)
```

The distance from a position to itself is zero. Passing `null` throws
`NullPointerException`.

## Current Scope

`Position2D` stores and validates coordinates and supports displacement and
distance queries. Movement and other geometry operations should be introduced
only when an active milestone requires them.
