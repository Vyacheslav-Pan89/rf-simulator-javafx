# PowerWatts

`PowerWatts` is an immutable domain value representing linear power.

## Canonical Unit

`PowerWatts` stores its value in watts. The `watts` record component and
accessor always use watts.

## Valid Values

- Values must be finite.
- Values must be greater than or equal to zero.
- Zero represents no power.
- Negative zero is accepted and stored as canonical positive zero.

Negative values, `NaN`, positive infinity, and negative infinity are invalid.
Constructing a `PowerWatts` with an invalid value throws
`IllegalArgumentException`.

## Equality

`PowerWatts` uses the value-based equality supplied by a Java record. Two
instances containing the same canonical watt value are equal. Positive zero
and negative zero therefore produce equal `PowerWatts` values.

## Conversion To Dbm

Positive watt values can be converted explicitly to `PowerDbm` using:

```text
dBm = 10 * log10(watts / 0.001)
```

Zero watts cannot be represented as a finite dBm value. Attempting to convert
zero watts to `PowerDbm` throws `IllegalStateException`.

Reference conversions:

| Watts | dBm |
| ---: | ---: |
| 0.001 | 0.0 |
| 1.0 | 30.0 |
| 10.0 | 40.0 |

## Current Scope

`PowerWatts` stores and validates watts and supports explicit conversion to
`PowerDbm`. Arithmetic operations should be introduced only when an active
milestone requires them.
