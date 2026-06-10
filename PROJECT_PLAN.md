# RF Simulator JavaFX Project Plan

This is a practical roadmap for the repository owner. Keep it lightweight: work on one milestone at a time, learn by implementing it personally, and leave tasks unchecked until they are complete.
This is a practical roadmap for the repository owner. Keep it lightweight: work on one milestone at a time, learn by implementing it personally, and leave tasks unchecked until they are complete. Use [`DESIGN_GUIDE.md`](DESIGN_GUIDE.md) for the target design, responsibility boundaries, and implementation review checklist.

## 1. Project purpose and non-goals

RF Simulator is an educational desktop application with two connected goals:

1. Learn professional Java software engineering through architecture, testing, incremental delivery, and documentation.
2. Learn RF engineering through simplified, explicit, documented, and testable propagation models.

The project should make both the software design and the RF reasoning understandable. A smaller model with clearly stated assumptions is more valuable here than a complicated model whose behavior cannot be explained or tested.

### Non-goals

- RF Simulator is **not** a professional electromagnetic solver, certification tool, or safety-analysis tool.
- It must not claim that simplified output predicts real installations accurately.
- Full-wave electromagnetic simulation, regulatory compliance analysis, and safety-critical decisions are outside the project scope.
- Advanced 3D rendering is not an early goal; begin with a simple 2D JavaFX Canvas view.
- Framework-heavy architecture, premature optimization, and abstractions without a current use are not goals.

## 2. Planned technology stack

The owner should plan around the following choices while introducing them only when an early milestone needs them:

- Java 21.
- JavaFX for the desktop presentation layer, initially using programmatic UI construction.
- Maven with the Maven Wrapper so builds do not depend on a globally installed Maven version.
@@ -43,52 +43,54 @@ The conceptual layers are:
Domain Core
    ↓
Simulation Engine
    ↓
Scene Model
    ↓
Application Layer
    ↓
Presentation Layer
```

The diagram shows conceptual construction from foundational concepts toward user-facing behavior. In source-code dependency terms, presentation code may call application code, application code may use scene and simulation concepts, and all dependencies should point toward lower-level policies. Lower layers must never import higher layers.

### Layer responsibilities

- **Domain Core:** immutable RF and mathematical concepts, values, invariants, and unit-aware operations. It must be independent of JavaFX.
- **Simulation Engine:** calculations that consume domain and scene inputs and produce immutable results. Simulation behavior must be deterministic and testable without launching JavaFX.
- **Scene Model:** toolkit-independent descriptions of sources, receivers, obstacles, terrain, and scenarios. It must not contain controls, colors, canvas objects, or other JavaFX types.
- **Application Layer:** use cases and commands that coordinate work and own application-level policies. It must not contain presentation or rendering logic.
- **Presentation Layer:** JavaFX-specific controls, windows, event handling, and rendering adapters. It translates user actions into application use cases and immutable results into visuals.

### Project-wide dependency rules

- Domain, simulation, and scene-model code must not depend on JavaFX.
- JavaFX-specific code belongs only in presentation or JavaFX visualization packages.
- Prefer immutable values and Java records where they make invariants and meaning clear.
- Make units explicit in every API involving physical quantities. Names such as `frequencyHz`, `distanceMeters`, `powerWatts`, and `fieldStrengthDbm` are preferable to ambiguous names.
- Prefer immutable values and Java records where they make invariants and meaning clear. Never expose mutable internal collections across layer boundaries.
- Make units explicit in every API involving physical quantities. Use distinct types for linear and logarithmic quantities, and distinguish positions from vectors. Names such as `frequencyHz`, `distanceMeters`, `powerWatts`, and `fieldStrengthDbm` are preferable to ambiguous names.
- Begin with one source concept and introduce additional source types only when they have genuinely different behavior or invariants.
- Keep grid definition, deterministic point generation, and sampled simulation results as separate responsibilities.
- Every RF formula must document its assumptions, units, valid range, boundary behavior, and limitations before it is treated as complete.
- Long-running calculations must never run on the JavaFX Application Thread. Introduce background execution only when work is actually expensive, and keep concurrency outside the simulation mathematics.
- Results passed to presentation code should be immutable snapshots so rendering cannot accidentally alter simulation state.
- Begin visualization with a simple 2D JavaFX Canvas. Defer advanced 3D until the educational value and cost are clearly understood.

These rules keep the mathematics testable, prevent UI concerns from distorting the model, and make future changes easier to reason about.

## 4. Recommended future package structure

The following packages are guidance for future milestones only; create them only when they gain a real responsibility:

| Future package | Intended responsibility |
| --- | --- |
| `com.rfsimulator.domain` | Immutable mathematical values, RF quantities, units, and core invariants. |
| `com.rfsimulator.simulation` | Propagation strategies, field sampling, calculations, and immutable simulation results. |
| `com.rfsimulator.scene` | Sources, receivers, obstacles, terrain, and complete scenarios without UI dependencies. |
| `com.rfsimulator.application` | Commands, use cases, orchestration, and ownership of mutable application state. |
| `com.rfsimulator.visualization` | Toolkit-independent normalization, ranges, legends, and presentation-ready values. |
| `com.rfsimulator.ui` | JavaFX windows, controls, Canvas rendering, event handling, and JavaFX-only adapters. |

Avoid creating empty packages merely to match the plan. Add a package when the current milestone gives it behavior or concepts to own.

## 5. Development workflow and milestone rules

1. Work on one milestone at a time; keep later ideas out of the current implementation.
@@ -157,119 +159,119 @@ Each milestone should produce one small, understandable result. Complete its TOD
- [ ] Create a minimal programmatically constructed window.
- [ ] Keep application startup separate from future simulation logic.
- [ ] Document the local run command.

**Not yet:** Final UI design, Canvas rendering, simulation, or background tasks.

**Done when:** `./mvnw javafx:run` opens the minimal window in a graphical environment and `./mvnw verify` still passes.

### Milestone 2 — Units and scalar RF values

**Goal:** Establish explicit unit conventions for later calculations.

- [ ] Define conventions for distance, frequency, and power.
- [ ] Design immutable scalar values with clear validation rules.
- [ ] Make linear and logarithmic power conversions explicit.
- [ ] Test equality, conversions, zero, negative, and non-finite inputs where relevant.

**Not yet:** Vectors, grids, sources, or propagation formulas.

**Done when:** Unit rules are documented, values are JavaFX-free, and boundary tests pass.

### Milestone 3 — Positions and vectors

**Goal:** Add the minimum immutable geometry vocabulary needed by a 2D simulator.

- [ ] Design immutable 2D positions and vectors.
- [ ] Define coordinate and distance conventions.
- [ ] Design separate immutable 2D position and vector concepts.
- [ ] Define coordinate, displacement, direction, and distance conventions.
- [ ] Add only operations required by the next milestone.
- [ ] Test equality, direction, distance, and boundary behavior.

**Not yet:** Grids, rendering, 3D coordinates, or simulation.

**Done when:** Geometry behavior is deterministic, documented, tested, and independent of JavaFX.

### Milestone 4 — Grid definition

**Goal:** Describe valid rectangular sampling grids without generating samples yet.

- [ ] Define bounds and resolution conventions.
- [ ] Decide whether bounds represent points, cells, or both.
- [ ] Define bounds and resolution or spacing conventions.
- [ ] Decide whether bounds represent points or cells and whether boundaries are included.
- [ ] Validate reversed, empty, and invalid bounds and resolutions.
- [ ] Test valid and invalid definitions.

**Not yet:** Point generation, rendering, or RF calculations.

**Done when:** Grid definitions have unambiguous invariants and passing boundary tests.

### Milestone 5 — Deterministic grid generation

**Goal:** Generate sampling positions in a documented, repeatable order.

- [ ] Generate positions from a valid grid definition.
- [ ] Document ordering and boundary inclusion rules.
- [ ] Generate positions from a valid grid definition without combining generation and result storage.
- [ ] Document ordering, floating-point boundary behavior, and boundary inclusion rules.
- [ ] Test sample counts, coordinates, ordering, and non-square grids.
- [ ] Check behavior at minimum and large practical resolutions.

**Not yet:** Rendering or propagation calculations.

**Done when:** Identical definitions always produce identical ordered samples.

### Milestone 6 — Source model and propagation contract

**Goal:** Define the inputs and output shape for one educational propagation calculation.

- [ ] Design one minimal immutable source type.
- [ ] Define a small propagation-model contract with explicit units.
- [ ] Design one minimal immutable source type without overlapping source representations.
- [ ] Define a small propagation-model contract whose input and output meanings and units are explicit.
- [ ] Decide and document behavior at or near a source.
- [ ] Keep all contracts independent of JavaFX.

**Not yet:** A propagation equation, grid sampling, or multiple source types.

**Done when:** The source and model contract clearly express units, boundaries, and responsibilities.

### Milestone 7 — First educational propagation model

**Goal:** Implement and understand one simple, clearly limited propagation equation.

- [ ] Select one educational model and document it using the RF model documentation rules.
- [ ] Implement the model without grid or UI dependencies.
- [ ] Add independently calculated reference cases.
- [ ] Test valid-range boundaries and singularity handling.
- [ ] State clearly that results are not professional-grade RF predictions.

**Not yet:** Grid sampling, multiple models, obstacles, or terrain.

**Done when:** The equation, assumptions, units, limitations, and deterministic tests agree.

### Milestone 8 — Field sampling and immutable results

**Goal:** Apply the first propagation model to a grid and produce a stable result snapshot.

- [ ] Sample the model at generated grid positions.
- [ ] Define an immutable simulation-result snapshot.
- [ ] Preserve a clear relationship between positions and sampled values.
- [ ] Define an immutable simulation-result snapshot with immutable collections.
- [ ] Preserve a clear ordered relationship between positions and sampled values.
- [ ] Test empty inputs, repeatability, ordering, and representative results.

**Not yet:** Color mapping, JavaFX rendering, or multiple sources.

**Done when:** Identical inputs produce identical immutable result snapshots without JavaFX.

### Milestone 9 — Visualization ranges and normalization

**Goal:** Convert simulation values into toolkit-independent presentation values.

- [ ] Define display ranges and normalization behavior.
- [ ] Handle empty, constant, clipped, minimum, and maximum ranges.
- [ ] Define toolkit-independent legend labels and color-map values.
- [ ] Test normalization boundaries and confirm simulation results remain unchanged.

**Not yet:** JavaFX Canvas or UI controls.

**Done when:** Presentation-ready snapshots are deterministic, immutable, and JavaFX-free.

### Milestone 10 — Static JavaFX Canvas view

**Goal:** Draw one known immutable result in a simple 2D view.

- [ ] Render a heatmap on JavaFX Canvas.
- [ ] Render source markers and a readable legend.
@@ -288,51 +290,51 @@ Each milestone should produce one small, understandable result. Complete its TOD
- [ ] Run expensive calculation work outside the JavaFX Application Thread.
- [ ] Publish only immutable snapshots to presentation code.
- [ ] Handle background success, failure, and cancellation where needed.

**Not yet:** Scenario editing or multiple propagation models.

**Done when:** Resizing remains clear and recalculation does not block the JavaFX Application Thread.

### Milestone 12 — Scene model

**Goal:** Represent a complete simulation scenario without UI dependencies.

- [ ] Define a minimal scene containing sources and required parameters.
- [ ] Decide which scene values are immutable and where mutable state will live.
- [ ] Test empty scenes and invalid scene definitions.
- [ ] Keep the scene model independent of JavaFX.

**Not yet:** UI editing commands, persistence, obstacles, or terrain.

**Done when:** A valid scene completely describes the inputs needed for the current simulation.

### Milestone 13 — Application commands and state ownership

**Goal:** Coordinate scene changes and simulation reruns without putting workflow logic in JavaFX controls.

- [ ] Define one clear owner for mutable application state.
- [ ] Define one clear owner for mutable application state without exposing mutable internal collections.
- [ ] Add small commands or use cases for source placement and parameter updates.
- [ ] Coordinate simulation reruns through the application layer.
- [ ] Test workflows below the UI.

**Not yet:** Persistence or advanced RF behavior.

**Done when:** A user intent can be traced through an application command to a new immutable result snapshot.

### Milestone 14 — Multiple sources and combination rules

**Goal:** Combine several sources using explicit, mathematically correct rules.

- [ ] Support multiple sources of the existing type.
- [ ] Document whether and where quantities combine linearly or logarithmically.
- [ ] Implement explicit conversions required by combination rules.
- [ ] Test source ordering, single-source equivalence, and reference combinations.

**Not yet:** Multiple propagation strategies or source categories.

**Done when:** Combination behavior is documented, deterministic, and tested without silent unit mixing.

### Milestone 15 — Interchangeable propagation strategies

**Goal:** Compare more than one educational model through a stable contract.

README.md
