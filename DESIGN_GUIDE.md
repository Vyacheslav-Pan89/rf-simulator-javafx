# RF Simulator Design Guide

This guide defines the intended architecture of RF Simulator. Use it with [`PROJECT_PLAN.md`](PROJECT_PLAN.md):

- `PROJECT_PLAN.md` defines **when** a capability is introduced.
- This guide defines **where it belongs**, **which module may depend on it**, and **which decisions must be made before implementation**.

Names in this guide are recommendations, not requirements. Prefer the smallest design that satisfies the active milestone. Do not create a module, package, class, or interface before it has a real responsibility.

## 1. Target behavior

The completed application should let a learner describe a two-dimensional educational RF scenario, run a documented propagation calculation, and inspect an understandable visualization of the result.

Later capabilities may include multiple sources, interchangeable educational propagation models, simplified obstacles and terrain, directional antenna patterns, radio-navigation lessons, versioned scenario files, probes, metrics, and comparison charts.

The application must always state that its results are educational. It is not a professional electromagnetic solver, certification tool, operational navigation tool, or safety-analysis tool.

## 2. Architectural dependency direction

Dependencies point toward lower-level policies. A lower-level module must never depend on a higher-level module.

```text
rf-domain
    ↑
rf-scene
    ↑
rf-simulation
    ↑
rf-application ────────┐
                       ├──> rf-desktop
rf-visualization ──────┘
```

The complete allowed dependency set is:

```text
rf-domain        -> no project modules
rf-scene         -> rf-domain
rf-simulation    -> rf-domain, rf-scene
rf-application   -> rf-domain, rf-scene, rf-simulation
rf-visualization -> rf-domain, rf-simulation
rf-desktop       -> rf-application, rf-visualization, JavaFX
rf-persistence   -> stable lower-level contracts chosen when persistence begins
```

This graph is intentionally acyclic. If two modules appear to require each other, responsibilities must be reconsidered instead of creating a cycle.

## 3. Incremental Maven module strategy

RF Simulator uses Maven modules to enforce major architectural boundaries. Modules are introduced incrementally when the active milestone gives them real behavior.

| Module | Primary package | Responsibility |
| --- | --- | --- |
| `rf-domain` | `com.rfsimulator.domain` | Immutable physical quantities, geometry, values, and core invariants |
| `rf-scene` | `com.rfsimulator.scene` | JavaFX-free descriptions of sources, grids, obstacles, terrain, and complete scenarios |
| `rf-simulation` | `com.rfsimulator.simulation` | Propagation contracts, deterministic calculations, sampling, and immutable results |
| `rf-application` | `com.rfsimulator.application` | Commands, use cases, workflow coordination, and mutable-state ownership |
| `rf-visualization` | `com.rfsimulator.visualization` | Toolkit-independent display ranges, normalization, legends, probes, and chart-ready data |
| `rf-desktop` | `com.rfsimulator.ui` and launcher package | JavaFX lifecycle, controls, events, Canvas drawing, and JavaFX adapters |
| `rf-persistence` | Chosen when needed | Versioned and validated scenario storage, only if a separate module is justified |

Apply these Maven rules:

1. The repository root POM is the parent and reactor aggregator.
2. Add a child module only when it receives its first real responsibility.
3. Do not create empty future modules to mirror the final architecture.
4. Child modules explicitly declare every project-module dependency they use.
5. The parent uses `dependencyManagement` and `pluginManagement` to centralize versions and defaults; it must not inject broad dependencies into every child.
6. Only `rf-desktop` declares JavaFX dependencies.
7. Do not create generic `common`, `shared`, or `utils` modules. Place a type according to its meaning.
8. Maven modules and Java Platform Module System descriptors are separate decisions. Do not add `module-info.java` without a documented need.
9. Run `./mvnw test` and `./mvnw verify` from the repository root to validate the complete active reactor.

## 4. Layer and package rules

1. `domain`, `scene`, `simulation`, `application`, and `visualization` must never import JavaFX.
2. UI event handlers translate user actions into application commands or use cases; they do not perform RF calculations.
3. Simulation code accepts immutable inputs and returns immutable result snapshots.
4. Presentation normalization is calculated outside JavaFX rendering code.
5. Mutable application state has one clear owner.
6. Expensive work may run in the background only after profiling shows a need; concurrency stays outside simulation mathematics.
7. Packages organize related classes inside modules. Modules enforce significant dependency boundaries.
8. If a class appears to belong to several packages or modules, it probably owns too many responsibilities and should be split.

## 5. Core design decisions

### 5.1 Physical quantities are not generic numbers

Represent quantities with explicit immutable types when doing so prevents unit confusion. Likely concepts include `Distance`, `Frequency`, `PowerWatts`, `PowerDbm`, `GainDb`, and `Angle`.

Requirements:

- Choose and document one canonical unit for each quantity.
- Validate non-finite values and invalid ranges at construction boundaries.
- Keep linear and logarithmic values in distinct types.
- Make conversions explicit and named.
- Do not pass ambiguous parameters such as `double power` when the required unit is unclear.

### 5.2 Positions and vectors have different meanings

Do not use one type interchangeably for both a location and a displacement.

Recommended concepts:

- `Position2D`: a location in the documented coordinate system.
- `Vector2D`: a direction or displacement.

Only add operations required by the active milestone. For example, `Position2D.distanceTo(Position2D)` is useful early, while rotations or projections should wait for a real feature.

### 5.3 Begin with one source concept

Begin with one minimal immutable source, likely named `RadioSource` or `PointSource`. It should express only the data required by the first propagation model, such as position and transmit power.

Do not create overlapping source representations with unclear differences. Introduce another source type only when its behavior or invariants are genuinely different.

Source descriptions belong in `rf-scene`; propagation behavior belongs in `rf-simulation`.

### 5.4 Separate grid definition, generation, and sampled results

These are separate responsibilities:

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

Do not combine source ownership, grid configuration, point generation, and sampled results into one large class.

### 5.5 Propagation models must state what they calculate

Use a small propagation contract so sampling workflows do not depend on one equation. A likely interface is `PropagationModel`, but introduce it only when its input and output meanings are understood.

Every model must define:

- the physical or educational meaning of its output;
- equation and units;
- assumptions and valid conditions;
- behavior at and near a source;
- omitted effects and limitations; and
- independently calculated reference cases.

Avoid vague names such as `FieldCalculator` unless the result truly represents a documented field quantity. Prefer names that communicate the model or output meaning.

### 5.6 Results are immutable snapshots

A result must preserve an unambiguous relationship between sample positions and values. A likely shape is an immutable collection of `FieldSample` values inside a `SimulationResult`.

Requirements:

- Callers cannot mutate simulation-owned collections.
- Identical inputs and configuration produce identical ordered results within a documented floating-point tolerance.
- Result metadata identifies the model and assumptions used.
- UI code can read a result but cannot alter it.

### 5.7 Application workflows own changes

The application layer coordinates user intentions and owns mutable application state.

```text
User action
    -> application command or use case
        -> new immutable scene snapshot
            -> simulation run
                -> immutable result snapshot
                    -> visualization preparation
                        -> JavaFX rendering
```

Return immutable copies or snapshots from state owners. Never expose mutable internal collections directly.

### 5.8 Visualization preparation is separate from drawing

`rf-visualization` converts simulation values into toolkit-independent display data. It may own display ranges, clipping rules, normalization, legend labels, probes, metric results, and chart-ready comparison data.

`rf-desktop` converts prepared data into JavaFX colors, Canvas operations, controls, and charts. Canvas code must not contain RF formulas or decide simulation-value normalization rules.

## 6. Candidate types by module

Introduce these only when their milestone begins, and adjust names when implemented behavior suggests something clearer.

| Module | Candidate types |
| --- | --- |
| `rf-domain` | `Distance`, `Frequency`, `PowerWatts`, `PowerDbm`, `GainDb`, `Angle`, `Position2D`, `Vector2D` |
| `rf-scene` | `RadioSource`, `GridDefinition`, `SimulationScene`, `Obstacle`, `TerrainProfile`, `AntennaPattern` |
| `rf-simulation` | `PropagationModel`, model implementations, `GridGenerator`, `FieldSampler`, `FieldSample`, `SimulationResult` |
| `rf-application` | `ApplicationState`, focused commands, and focused use cases |
| `rf-visualization` | `DisplayRange`, `ValueNormalizer`, `Legend`, `ProbeResult`, `ModelComparison` |
| `rf-desktop` | `RfSimulatorLauncher`, `RfSimulatorApplication`, main window/view, Canvas renderer, and focused editor panes |
| `rf-persistence` | Scenario document, format version, serializer, and repository, if justified |

Interfaces are appropriate where behavior is genuinely interchangeable, such as propagation models or antenna patterns. Prefer records and concrete classes for simple values and single implementations. Do not create an interface only to cross a module boundary.

## 7. Feature placement rules

| Question | Destination |
| --- | --- |
| Is it an immutable RF value, unit, geometry concept, or invariant? | `rf-domain` |
| Does it describe something present in a scenario? | `rf-scene` |
| Does it calculate RF behavior or sample a model? | `rf-simulation` |
| Does it coordinate a user intent or own mutable state? | `rf-application` |
| Does it prepare ranges, normalized values, legends, probes, or chart data? | `rf-visualization` |
| Does it import JavaFX or draw/control something? | `rf-desktop` |
| Does it encode or decode a stable scenario format? | `rf-persistence`, if separately justified |

If several modules need a type, place it in the lowest module that truly owns its meaning. Do not move a type downward only to avoid declaring a legitimate dependency.

## 8. Engineering principles

### 8.1 Test-driven development

Prefer a red-green-refactor cycle for deterministic domain, scene, simulation, visualization, and application behavior:

1. Write one readable failing test that describes the next behavior.
2. Implement the smallest change that makes the test pass.
3. Refactor while keeping all tests passing.
4. Run the complete active Maven reactor before completing the milestone.

Strict TDD is not required for exploratory JavaFX layout work. Keep calculations, normalization, validation, and workflow decisions below JavaFX so those behaviors can still be developed and tested deterministically.

### 8.2 KISS and YAGNI

Implement the smallest understandable design that satisfies the active milestone. Do not add future modules, interfaces, behavior, frameworks, concurrency, or optimization without a current documented need.

### 8.3 Responsible DRY

Remove duplication when it represents the same stable knowledge or rule. Prefer small local duplication over a shared abstraction with unclear ownership or behavior that is likely to diverge.

### 8.4 Practical SOLID

Apply SOLID principles when they improve a current responsibility boundary, dependency direction, or substitutable behavior. Do not create interfaces, inheritance hierarchies, layers, or modules only to satisfy a principle mechanically.

### 8.5 Practical clean code

For this project, clean code means:

- names communicate responsibility and physical units;
- classes and methods remain focused;
- validation and boundary behavior are explicit;
- dependencies follow the documented acyclic module graph;
- comments explain RF assumptions and non-obvious decisions rather than syntax;
- tests remain readable examples of intended behavior; and
- refactoring improves clarity without changing protected behavior.

These principles guide decisions rather than acting as absolute rules. Clarity, correctness, and the active milestone's scope take priority over satisfying an acronym.

## 9. Implementation sequence

For each milestone:

1. Write down the smallest behavior and explicit non-goals.
2. Identify the module that owns the behavior; create that module only if it does not exist and now has a real responsibility.
3. Define units, invariants, invalid inputs, and boundary behavior before implementation.
4. Add the smallest type or contract needed by that behavior.
5. Write readable tests as examples of intended behavior.
6. Implement only enough to satisfy the active milestone.
7. Run `./mvnw test` during development and `./mvnw verify` before completion.
8. Update documentation and mark TODOs complete only when the milestone completion statement is true.
