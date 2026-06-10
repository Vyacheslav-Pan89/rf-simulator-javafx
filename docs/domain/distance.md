# Distance

`Distance` is an immutable domain value representing a non-negative physical
distance.

## Canonical Unit

`Distance` stores its value in meters. The `meters` record component and
accessor always use meters.

## Valid Values

- Values must be finite.
- Values must be greater than or equal to zero.
- Zero represents no separation.

Negative values, `NaN`, positive infinity, and negative infinity are invalid.
Constructing a `Distance` with an invalid value throws
`IllegalArgumentException`.

## Equality

`Distance` uses the value-based equality supplied by a Java record. Two
instances containing the same meter value are equal.

## Current Scope

`Distance` currently stores and validates meters only. Unit conversions and
arithmetic operations should be introduced only when an active milestone
requires them.
