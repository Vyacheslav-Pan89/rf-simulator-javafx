# Frequency

`Frequency` is an immutable domain value representing a positive radio
frequency.

## Canonical Unit

`Frequency` stores its value in hertz. The `hertz` record component and
accessor always use hertz.

## Valid Values

- Values must be finite.
- Values must be greater than zero.

Zero, negative zero, negative values, `NaN`, positive infinity, and negative
infinity are invalid. Constructing a `Frequency` with an invalid value throws
`IllegalArgumentException`.

Zero is excluded because this value represents radio frequency rather than a
general signal frequency that could include direct current.

## Equality

`Frequency` uses the value-based equality supplied by a Java record. Two
instances containing the same hertz value are equal.

## Current Scope

`Frequency` currently stores and validates hertz only. Unit conversions and
arithmetic operations should be introduced only when an active milestone
requires them.
