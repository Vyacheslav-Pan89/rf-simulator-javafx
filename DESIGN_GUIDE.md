# RF Simulator Design Guide

This guide describes the intended shape of RF Simulator as it grows. It turns the project roadmap into practical design instructions without requiring every future class to be created in advance.

Use this document together with [`PROJECT_PLAN.md`](PROJECT_PLAN.md):

- `PROJECT_PLAN.md` defines **when** a capability should be introduced.
- This guide defines **where it belongs**, **how layers communicate**, and **which design decisions must be made before implementation**.

Names in this guide are recommendations, not requirements. Prefer the smallest design that satisfies the current milestone, and create a class or interface only when it has a real responsibility.

## 1. Target behavior

The completed application should let a learner describe a two-dimensional educational RF scenario, run a documented propagation calculation, and inspect an understandable visualization of the result.

Later capabilities may include multiple sources, interchangeable educational propagation models, simplified obstacles and terrain, directional antenna patterns, radio-navigation lessons, versioned scenario files, probes, metrics, and comparison charts.

The application must always state that its results are educational. It is not a professional electromagnetic solver, certification tool, operational navigation tool, or safety-analysis tool.

## 2. Dependency direction

Dependencies must point toward lower-level policies:

```text
JavaFX UI
    -> Application workflows
        -> Scene descriptions and simulation services
            -> Domain values and mathematics
```

Apply these rules:

1. `domain`, `simulation`, and `scene` must never import JavaFX.
2. UI event handlers translate user actions into application commands or use cases; they do not perform RF calculations.
3. Simulation code accepts immutable inputs and returns immutable result snapshots.
4. Presentation normalization is calculated outside JavaFX rendering code.
5. Mutable state has one clear owner in the application layer.
6. Expensive work may run in the background only after profiling shows a need; concurrency must remain outside simulation mathematics.

## 3. Intended package responsibilities

```text
com.rfsimulator
├── domain          immutable units, values, geometry, and invariants
├── simulation      propagation contracts, calculations, sampling, and results
├── scene           toolkit-independent scenario descriptions
├── application     commands, use cases, and mutable-state ownership
├── visualization   toolkit-independent display ranges and normalized values
└── ui              JavaFX lifecycle, controls, Canvas drawing, and event adapters
```

Add packages only when the active milestone gives them behavior to own. Do not create an empty final-project package tree.

A separate persistence package may be introduced when versioned scenario persistence begins.

## 4. Core design decisions

### 4.1 Physical quantities are not generic numbers

Represent quantities with explicit immutable types when doing so prevents unit confusion. Likely concepts include `Distance`, `Frequency`, `PowerWatts`, `PowerDbm`, `GainDb`, and `Angle`.

Requirements:

- Choose and document one canonical unit for each quantity.
- Validate non-finite values and invalid ranges at construction boundaries.
- Keep linear and logarithmic values in distinct types.
- Make conversions explicit and named.
- Do not pass ambiguous parameters such as `double power` when the required unit is not obvious from the type or name.

### 4.2 Positions and vectors have different meanings

Do not use one type interchangeably for both a location and a displacement.

Recommended concepts:

- `Position2D`: a location in the documented two-dimensional coordinate system.
- `Vector2D`: a direction or displacement.

Only add operations required by the current milestone. For example, `Position2D.distanceTo(Position2D)` is useful early, while rotations or projections should wait until a real feature needs them.

### 4.3 Use one source concept until distinct source categories are needed

Begin with one minimal immutable source, likely named `RadioSource` or `PointSource`. It should express only the data required by the first propagation model, such as position and transmit power.

Do not create overlapping source classes with unclear differences. Introduce another source type only when its behavior or invariants are genuinely different.

Source descriptions belong in `scene`; propagation behavior belongs in `simulation`.

### 4.4 Separate grid definition, generation, and sampled results

These are different responsibilities:

- A grid definition describes valid bounds and resolution or spacing.
- A grid generator creates positions in a documented deterministic order.
- A simulation result associates each generated position with a calculated value.

Before implementing grid generation, document:

- whether bounds describe points or cells;
- whether minimum and maximum boundaries are included;
- whether resolution means sample count or spacing;
- ordering of generated points;
- behavior for reversed, empty, non-finite, and invalid definitions; and
- floating-point boundary behavior.

Do not combine source ownership, grid configuration, generation, and sampled results into one large grid class.

### 4.5 Propagation models must state what they calculate

Use a small propagation contract so sampling workflows do not depend on one equation. A likely interface is `PropagationModel`, but it should be introduced only when its input and output meanings are understood.

Every model must define:

- the physical or educational meaning of its output;
- equation and units;
- assumptions and valid conditions;
- behavior at and near a source;
- omitted effects and limitations; and
- independently calculated reference cases.

Avoid vague names such as `FieldCalculator` unless the result truly represents a documented field quantity. Prefer a name that communicates the model or output meaning.

### 4.6 Results are immutable snapshots

A result should preserve an unambiguous relationship between sample positions and values. A likely shape is an immutable collection of `FieldSample` values inside a `SimulationResult`.

Requirements:

- Callers cannot mutate simulation-owned collections.
- Identical inputs and configuration produce identical ordered results within a documented floating-point tolerance.
- Result metadata identifies the model and assumptions used.
- UI code can read a result but cannot alter it.

### 4.7 Application workflows own changes

The application layer is the single coordination point between UI actions and lower-level behavior.

A typical flow is:

```text
User action
    -> application command or use case
        -> new immutable scene snapshot
            -> simulation run
                -> immutable result snapshot
                    -> visualization preparation
                        -> JavaFX rendering
```

Return immutable copies or snapshots from state owners. Never expose a mutable internal collection directly.

### 4.8 Visualization preparation is separate from drawing

`visualization` converts simulation values into toolkit-independent display data. It may own display ranges, clipping rules, normalization, legend labels, probes, metric results, and chart-ready comparison data.

`ui` converts that prepared data into JavaFX colors, Canvas operations, controls, and charts. Canvas code must not contain RF formulas or decide simulation-value normalization rules.

## 5. Candidate types by layer

The following names communicate the intended responsibilities. Introduce them only when their milestone begins and adjust names when the implemented behavior suggests something clearer.

| Layer | Candidate types | Responsibility |
| --- | --- | --- |
| Domain | `Distance`, `Frequency`, `PowerWatts`, `PowerDbm`, `GainDb`, `Angle` | Explicit immutable physical quantities and conversions |
| Domain | `Position2D`, `Vector2D` | Locations, directions, displacement, and required geometry |
| Scene | `RadioSource`, `GridDefinition`, `SimulationScene`, `Obstacle`, `TerrainProfile`, `AntennaPattern` | Complete JavaFX-free descriptions of scenario inputs |
| Simulation | `PropagationModel`, model implementations, `GridGenerator`, `FieldSampler`, `FieldSample`, `SimulationResult` | Deterministic calculations and immutable outputs |
| Application | `ApplicationState`, focused commands, focused use cases | Workflow coordination and ownership of mutable state |
| Visualization | `DisplayRange`, `ValueNormalizer`, `Legend`, `ProbeResult`, `ModelComparison` | Deterministic presentation-ready values |
| UI | `RfSimulatorApplication`, main window/view, Canvas renderer, focused editor panes | JavaFX lifecycle, controls, events, and drawing |
| Persistence | scenario document, format version, serializer/repository | Versioned and validated save/load behavior |

Interfaces are justified where behavior is genuinely interchangeable, such as propagation models or antenna patterns. Prefer records and concrete classes for simple values and single implementations.

## 6. Feature placement rules

When adding a feature, place it by responsibility rather than by the screen where it appears.

| Question | Destination |
| --- | --- |
| Is it an immutable RF value, unit, or geometry invariant? | `domain` |
| Does it describe something present in a scenario? | `scene` |
| Does it calculate RF behavior or sample a model? | `simulation` |
| Does it coordinate a user intent or own mutable state? | `application` |
| Does it prepare ranges, normalized values, legends, probes, or chart data? | `visualization` |
| Does it import JavaFX or draw/control something? | `ui` |

If a class appears to belong in several packages, it probably owns too many responsibilities and should be split.

## 7. Implementation sequence

For each milestone:

1. Write down the smallest behavior and explicit non-goals.
2. Define units, invariants, invalid inputs, and boundary behavior before implementation.
3. Add the smallest type or contract needed by that behavior.
4. Write readable tests as examples of the intended behavior.
5. Implement only enough to satisfy the active milestone.
6. Run `./mvnw test` during development and `./mvnw verify` before completion.
7. Update documentation and mark TODOs complete only when the milestone completion statement is true.

Do not copy a future target structure into the repository in advance. Let tests and current responsibilities drive each addition.

- [ ] No interface, framework, concurrency, or abstraction was added without a current need.
- [ ] Tests cover normal behavior, invalid inputs, and relevant boundaries.
- [ ] Educational and non-operational limitations remain visible.
