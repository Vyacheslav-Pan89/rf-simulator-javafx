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
The conceptual layers are:
```
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
- [ ] Create a minimal programmatically constructed window.
- [ ] Keep application startup separate from future simulation logic.
- [ ] Document the local run command.

**Not yet:** Final UI design, Canvas rendering, simulation, or background tasks.

**Done when:** `./mvnw javafx:run` opens the minimal window in a graphical environment and `./mvnw verify` still passes.

## 6. Testing strategy

Testing should concentrate below the JavaFX layer, where behavior is fast and deterministic to verify.

- Write unit tests for values, formulas, grids, validation rules, and model boundaries.
- Use deterministic reference cases for simulation output. Given identical inputs and configuration, results should be identical within a documented floating-point tolerance.
- Choose tolerances deliberately for floating-point comparisons. Explain whether a test uses absolute tolerance, relative tolerance, or both, and why.
- Add architecture checks when useful to prevent JavaFX dependencies from entering domain, simulation, or scene packages.
- Add integration tests for application workflows only when unit tests cannot give sufficient confidence.
- Keep UI testing minimal. Test calculations, normalization, and workflow decisions beneath JavaFX, and manually validate essential rendering behavior.
- Include explicit edge cases where relevant: zero and negative values, invalid or reversed bounds, unsupported units, empty scenes, empty grids, extreme resolutions, and sample points at or near a source.
@@ -129,106 +134,123 @@ Before completing any RF model, document all of the following near its design or
- Singularity and boundary handling, including behavior at or near a source.
- Expected qualitative behavior, such as whether output should decrease with distance.
- Known limitations and effects deliberately omitted.
- One or more reference cases used by automated tests, including the source of expected values.

Use unambiguous names such as `frequencyHz`, `distanceMeters`, `powerWatts`, and `fieldStrengthDbm` where practical. Never mix linear and logarithmic quantities silently: conversions between watts, milliwatts, dBW, dBm, ratios, and decibels must be explicit, named, documented, and tested. Keep the educational disclaimer visible wherever model results could be misunderstood.

  ## 8. Engineering principles

- Prefer a red-green-refactor TDD cycle for deterministic domain, scene, simulation, visualization, and application behavior.
- Keep designs simple and limited to the active milestone by applying KISS and YAGNI.
- Remove duplication when it represents the same stable knowledge or rule; prefer small local duplication over a premature shared abstraction.
- Apply SOLID principles when they clarify a current responsibility or dependency boundary, not as a reason to add unnecessary interfaces or layers.
- Refactor after behavior is protected by tests.
- Treat readable names, explicit units, focused responsibilities, clear validation, and documented boundary behavior as the project's practical definition of clean code.

These principles guide decisions rather than acting as mechanical requirements. Clarity and correctness for the current milestone take priority over satisfying an acronym.

## 9. Milestone roadmap

Each milestone should produce one small, understandable result. Complete its TODOs and checks before moving on. If a milestone still feels too large while working on it, split it again rather than expanding its scope.

### Milestone 0 — Maven and test foundation

**Goal:** Establish a reproducible Java 21 build before adding JavaFX or RF concepts.

- [x] Research compatible Java 21, Maven plugin, and JUnit 5 versions.
- [x] Configure one Maven module and the `com.rfsimulator` base package.
- [x] Add and verify the Maven Wrapper.
- [x] Add one small non-UI JUnit 5 test.
- [x] Document local build and test commands.

**Not yet:** JavaFX, RF behavior, grids, persistence, or rendering.

**Done when:** A fresh checkout can run `./mvnw test` and `./mvnw verify` with Java 21.

### Milestone 0.5 — Multi-module build foundation

**Goal:** Establish an incremental Maven reactor that enforces the first architectural boundary.

- [ ] Convert the root POM into a parent and reactor aggregator.
- [ ] Create `rf-desktop` and move the current launcher, UI placeholder, and foundation test into it.
- [ ] Centralize dependency and plugin versions without injecting JavaFX into lower-level modules.
- [ ] Document how future modules are introduced only when they receive real responsibilities.
- [ ] Confirm the complete active reactor builds from the repository root.

**Not yet:** Empty domain, scene, simulation, application, visualization, or persistence modules; JPMS descriptors; RF behavior.

**Done when:** A fresh checkout can run `./mvnw test` and `./mvnw verify` from the root, and all current source belongs to `rf-desktop`.

### Milestone 1 — Minimal JavaFX application

**Goal:** Learn the JavaFX application lifecycle with the smallest possible window.

- [ ] Add JavaFX Controls and the JavaFX Maven Plugin to `rf-desktop` only.
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

- [ ] Add a second propagation model only after documenting its educational purpose.
- [ ] Make model selection explicit.
- [ ] Attach assumptions, units, valid range, and limitations as model metadata.
- [ ] Add comparison and reference tests.

**Not yet:** Obstacles, terrain, antenna patterns, or navigation systems.

**Done when:** Models can be selected without changing sampling workflows and their differences can be explained.

### Milestone 16 — Obstacles and simple attenuation

**Goal:** Add one limited environmental effect without pretending to perform full-wave simulation.

- [ ] Define one simple obstacle representation.
- [ ] Add one documented attenuation rule.
- [ ] Test the effect disabled, enabled, and at geometric boundaries.
- [ ] State omitted physical effects and model limitations prominently.

**Not yet:** Terrain, antenna patterns, or realistic material databases.

**Done when:** The obstacle effect can be explained, isolated, and tested deterministically.

### Milestone 17 — Terrain model

**Goal:** Introduce one simplified terrain effect separately from obstacles.

- [ ] Define the minimum terrain representation needed by the selected effect.
- [ ] Document geometry, units, assumptions, and limitations.
- [ ] Add isolated terrain-effect tests.
- [ ] Define how terrain and obstacle effects interact, if they do.

**Not yet:** Full terrain engines or full-wave simulation.

**Done when:** Terrain behavior is independently understandable and tested.

### Milestone 18 — Antenna gain patterns

**Goal:** Add directional source behavior with explicit angle and gain conventions.

- [ ] Define angle orientation and gain units.
- [ ] Add one simple immutable antenna pattern.
- [ ] Document interpolation and boundary behavior.
- [ ] Test expected directions and pattern boundaries.

**Not yet:** Professional antenna modeling or large pattern libraries.

**Done when:** Directional behavior is documented, deterministic, and independently tested.

### Milestone 19 — Educational radio-navigation scenario

**Goal:** Apply stable core concepts to one carefully scoped navigation-system lesson.

- [ ] Choose one of ILS, VOR, or DME based on a clear learning objective.
- [ ] Research and document the real-world concept using reputable references.
- [ ] Document every simulator simplification and non-operational limitation.
- [ ] Build one scenario using existing lower-level models.
- [ ] Add qualitative and reference tests where practical.

**Not yet:** The other navigation systems or operational guidance.

**Done when:** The scenario teaches one concept clearly and cannot be mistaken for an operational tool.

### Milestone 20 — Additional radio-navigation scenarios

**Goal:** Add the remaining educational ILS, VOR, or DME scenarios one at a time.

- [ ] Add only one new navigation scenario per focused change.
- [ ] Reuse stable lower-level models instead of bypassing architecture.
- [ ] Document real concepts, simplifications, and limitations for each scenario.
- [ ] Add suitable qualitative and reference tests.

**Not yet:** Certification, operational guidance, or flight-safety decisions.

**Done when:** Each added scenario is independently documented, understandable, and tested.

### Milestone 21 — Versioned scenario persistence

**Goal:** Save and reload stable educational scenarios without losing their meaning.

- [ ] Decide whether persistence belongs in `rf-application` or justifies a separate `rf-persistence` module.
- [ ] Design a versioned scenario format.
- [ ] Preserve units and model metadata explicitly.
- [ ] Validate imports and report unsupported or invalid data clearly.
- [ ] Test representative round trips, malformed data, and unsupported versions.

**Not yet:** Charts, probes, or broad compatibility promises.

**Done when:** Supported scenarios round-trip reliably and invalid input fails safely.

### Milestone 22 — Probes and coverage metrics

**Goal:** Add small analysis tools that help explain simulation results.

- [ ] Add one probe or metric at a time.
- [ ] State units, calculation source, assumptions, and limitations.
- [ ] Test calculations and boundary behavior below the UI.
- [ ] Keep analysis output educational and non-certifying.

**Not yet:** Charts or a large engineering-tool suite.

**Done when:** Each probe or metric is documented, deterministic, and useful for learning.

### Milestone 23 — Charts and comparison tools

**Goal:** Present stable results and model comparisons in focused educational charts.

- [ ] Add only charts that answer a clear learning question.
- [ ] Label units, models, assumptions, and ranges.
- [ ] Keep chart-data preparation testable below JavaFX.
- [ ] Validate charts against known result snapshots.

**Not yet:** Professional reporting or certification output.

**Done when:** Each chart communicates a documented learning point without obscuring model limitations.
