# Two-Dimensional Geometry

The simulator uses immutable two-dimensional Cartesian geometry for positions,
displacements, and distances.

## Coordinate System

- Coordinates and vector components are measured in meters.
- Positive x points right or east.
- Positive y points up or north.
- Negative coordinates and vector components are valid.
- All coordinates and vector components must be finite.

This coordinate system describes an educational two-dimensional plane. It
does not currently represent latitude, longitude, altitude, terrain, or a
specific geographic coordinate reference system.

## Positions And Vectors

`Position2D` and `Vector2D` have different meanings:

- `Position2D` represents a location in the coordinate system.
- `Vector2D` represents a displacement or direction.

A position must not be used as a vector, and a vector must not be used as a
position.

For example, the displacement from position `(10, 20)` meters to position
`(13, 24)` meters is vector `(3, 4)` meters. The distance between the
positions and the vector magnitude are both `5` meters.

## Current Scope

Current geometry supports only the operations required to express displacement
and distance between positions. Addition, movement, normalization, rotation,
angles, dot products, projections, and other geometry operations should be
introduced only when an active milestone requires them.
