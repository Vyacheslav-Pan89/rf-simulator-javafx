# PowerDbm

`PowerDbm` is an immutable domain value representing logarithmic power
relative to one milliwatt.

## Canonical Unit

`PowerDbm` stores its value in decibel-milliwatts (dBm). The `dbm` record
component and accessor always use dBm.

## Valid Values

- Values must be finite.
- Values may be positive, zero, or negative.
- Zero dBm represents one milliwatt, not zero power.
- Negative zero is accepted and stored as canonical positive zero.

`NaN`, positive infinity, and negative infinity are invalid. Constructing a
`PowerDbm` with an invalid value throws `IllegalArgumentException`.

## Equality

`PowerDbm` uses the value-based equality supplied by a Java record. Two
instances containing the same canonical dBm value are equal. Positive zero
and negative zero therefore produce equal `PowerDbm` values.

## Conversion To Watts

Every finite dBm value can be converted explicitly to `PowerWatts` using:

```text
watts = 0.001 * 10^(dBm / 10)
```

Reference conversions:

| dBm | Watts |
| ---: | ---: |
| 0.0 | 0.001 |
| 30.0 | 1.0 |
| 40.0 | 10.0 |

Floating-point calculations may produce small rounding differences from the
mathematical result. Conversion tests should therefore use a deliberately
chosen tolerance.

## Current Scope

`PowerDbm` stores and validates dBm and supports explicit conversion to
`PowerWatts`. Arithmetic operations and other logarithmic power units should
be introduced only when an active milestone requires them.
