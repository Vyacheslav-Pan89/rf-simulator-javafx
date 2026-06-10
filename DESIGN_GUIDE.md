# RF Simulator JavaFX Project Plan

This is a practical roadmap for the repository owner. Keep it lightweight: work on one milestone at a time, learn by implementing it personally, and leave tasks unchecked until they are complete. Use [`DESIGN_GUIDE.md`](DESIGN_GUIDE.md) for the target module design, dependency boundaries, responsibility placement, and implementation review checklist.

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
- JUnit 5 for automated tests.
- Base package `com.rfsimulator`.
- An incremental Maven multi-module build that enforces major architectural boundaries.
- Introduce a Maven module only when the active milestone gives it real behavior; do not create empty future modules.
- Keep JavaFX dependencies isolated in `rf-desktop`.
- No Spring or dependency-injection framework. Prefer explicit constructors and straightforward object creation.
- No FXML initially unless the owner later identifies and records a clear benefit.
- Maven modules run on the classpath initially; add `module-info.java` only if a separate documented need for JPMS appears.

Possible future Maven setup items include JavaFX Controls, JavaFX Maven Plugin, Maven Compiler Plugin, Maven Surefire Plugin, Maven Enforcer Plugin, and JUnit Jupiter. The owner should research current compatible versions, understand each tool's purpose, and add only what the current milestone needs.

## Engineering principles

### KISS and YAGNI

Implement the smallest understandable design that satisfies the active
milestone. Do not add future modules, interfaces, behavior, or optimization
without a current need.

### Responsible DRY

Remove duplication when it represents the same stable knowledge or business
rule. Prefer small local duplication over an abstraction with unclear
ownership or likely future divergence.

### Practical SOLID

Use SOLID principles to improve current responsibility boundaries and
substitutability. Do not create interfaces, inheritance hierarchies, or
layers only to satisfy a principle mechanically.

### Test-driven development

Prefer a red-green-refactor cycle for deterministic behavior:

1. Write one readable failing test that describes the next behavior.
2. Implement the smallest change that makes it pass.
3. Refactor while keeping all tests passing.
4. Run the complete active reactor before completing the milestone.

UI exploration does not require strict TDD, but calculations, normalization,
validation, and workflow decisions must remain below JavaFX and be tested
there.

### Practical clean code

For this project, clean code means:

- names communicate responsibility and physical units;
- methods and classes remain focused;
- validation and boundary behavior are explicit;
- dependencies follow the documented module graph;
- comments explain RF assumptions and non-obvious decisions;
- tests remain readable examples of intended behavior; and
- refactoring improves clarity without changing protected behavior.

## 3. Architectural modules and dependency rules

Maven modules enforce the major architecture boundaries. Add them incrementally when a milestone gives them real behavior.

The intended acyclic project-module dependencies are:

```text
rf-domain        -> no project modules
rf-scene         -> rf-domain
rf-simulation    -> rf-domain, rf-scene
rf-application   -> rf-domain, rf-scene, rf-simulation
rf-visualization -> rf-domain, rf-simulation
rf-desktop       -> rf-application, rf-visualization, JavaFX
rf-persistence   -> stable lower-level contracts chosen when persistence begins
```

### Module responsibilities

- **`rf-domain`:** immutable RF and mathematical concepts, values, invariants, and unit-aware operations. It must be independently testable and have no project-module dependency.
- **`rf-scene`:** toolkit-independent descriptions of sources, grids, receivers, obstacles, terrain, and complete scenarios.
- **`rf-simulation`:** propagation contracts and deterministic calculations that consume domain and scene inputs and produce immutable results.
- **`rf-application`:** use cases and commands that coordinate work and own application-level policies and mutable state.
- **`rf-visualization`:** toolkit-independent display ranges, normalization, legends, probes, metrics, and chart-ready values.
- **`rf-desktop`:** JavaFX lifecycle, controls, windows, events, Canvas drawing, and JavaFX adapters.
- **`rf-persistence`:** optional versioned scenario storage introduced only if the persistence milestone justifies a separate module.

### Project-wide dependency rules

- Lower-level modules must never depend on higher-level modules, and project-module cycles are forbidden.
- Only `rf-desktop` may declare JavaFX dependencies or import JavaFX types.
- Child modules explicitly declare the project modules they use; the parent POM manages versions and defaults without injecting broad dependencies.
- Do not create generic `common`, `shared`, or `utils` modules.
- Prefer immutable values and Java records where they make invariants and meaning clear. Never expose mutable internal collections across boundaries.
- Make units explicit in every API involving physical quantities. Use distinct types for linear and logarithmic quantities, and distinguish positions from vectors.
- Begin with one source concept and introduce additional source types only when they have genuinely different behavior or invariants.
- Keep grid definition, deterministic point generation, and sampled simulation results as separate responsibilities.
- Every RF formula must document assumptions, units, valid range, boundary behavior, limitations, and reference cases before it is complete.
- Introduce background execution only when work is actually expensive, and keep concurrency outside simulation mathematics.
- Results passed toward presentation code must be immutable snapshots.
- Begin visualization with a simple 2D JavaFX Canvas. Defer advanced 3D until its educational value and cost are understood.

These rules keep mathematics testable, prevent JavaFX concerns from entering lower layers, and allow the core behavior to remain reusable outside the desktop presentation.

## 4. Incremental module introduction

Do not create the final module tree in advance. Introduce each module when it receives the first real responsibility assigned by the active milestone.

| Module | Expected introduction |
| --- | --- |
| `rf-desktop` | Milestone 0.5 — multi-module build foundation |
| `rf-domain` | Milestone 2 — units and scalar RF values |
| `rf-scene` | Milestone 4 — grid definition |
| `rf-simulation` | Milestone 5 — deterministic grid generation |
| `rf-visualization` | Milestone 9 — visualization ranges and normalization |
| `rf-application` | Milestone 13 — application commands and state ownership |
| `rf-persistence` | Milestone 21 — only if a separate persistence module is justified |

Packages organize related types inside modules. Add a package only when the active milestone gives it behavior or concepts to own.

## 5. Development workflow and milestone rules

1. Work on one milestone at a time; keep later ideas out of the current implementation.
2. Implement and test small, understandable slices.
3. Write tests alongside domain and simulation behavior.
4. Use the Maven Wrapper: run `./mvnw test` during development and `./mvnw verify` before completing a milestone.
5. Run `./mvnw javafx:run` for manual UI checks. A failure caused only by a headless environment is an environment limitation, not an application failure.
6. Keep commits small and focused.
7. Review every dependency before adding it.
8. Preserve explicit units and deterministic results.
9. Profile before optimizing; avoid premature concurrency, advanced rendering, and abstraction.
10. Update this plan only when project scope genuinely changes.

## 6. Testing strategy

Testing should concentrate below the JavaFX layer, where behavior is fast and deterministic to verify.

- Write unit tests for values, formulas, grids, validation rules, and model boundaries.
- Use deterministic reference cases for simulation output. Given identical inputs and configuration, results should be identical within a documented floating-point tolerance.
- Choose tolerances deliberately for floating-point comparisons. Explain whether a test uses absolute tolerance, relative tolerance, or both, and why.
- Add architecture checks when useful to prevent JavaFX dependencies from entering domain, simulation, or scene packages.
- Add integration tests for application workflows only when unit tests cannot give sufficient confidence.
- Keep UI testing minimal. Test calculations, normalization, and workflow decisions beneath JavaFX, and manually validate essential rendering behavior.
- Include explicit edge cases where relevant: zero and negative values, invalid or reversed bounds, unsupported units, empty scenes, empty grids, extreme resolutions, and sample points at or near a source.
- Singularity and boundary handling, including behavior at or near a source.
- Expected qualitative behavior, such as whether output should decrease with distance.
- Known limitations and effects deliberately omitted.
- One or more reference cases used by automated tests, including the source of expected values.

Use unambiguous names such as `frequencyHz`, `distanceMeters`, `powerWatts`, and `fieldStrengthDbm` where practical. Never mix linear and logarithmic quantities silently: conversions between watts, milliwatts, dBW, dBm, ratios, and decibels must be explicit, named, documented, and tested. Keep the educational disclaimer visible wherever model results could be misunderstood.

## 8. Milestone roadmap

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

- [ ] Create `rf-domain` with no project-module dependencies.
- [ ] Define conventions for distance, frequency, and power.
- [ ] Design immutable scalar values with clear validation rules.
- [ ] Make linear and logarithmic power conversions explicit.
- [ ] Test equality, conversions, zero, negative, and non-finite inputs where relevant.

**Not yet:** Vectors, grids, sources, or propagation formulas.

**Done when:** Unit rules are documented, values are JavaFX-free, and boundary tests pass.

### Milestone 3 — Positions and vectors

**Goal:** Add the minimum immutable geometry vocabulary needed by a 2D simulator.

- [ ] Design separate immutable 2D position and vector concepts.
- [ ] Define coordinate, displacement, direction, and distance conventions.
- [ ] Add only operations required by the next milestone.
- [ ] Test equality, direction, distance, and boundary behavior.

**Not yet:** Grids, rendering, 3D coordinates, or simulation.

**Done when:** Geometry behavior is deterministic, documented, tested, and independent of JavaFX.

### Milestone 4 — Grid definition

**Goal:** Describe valid rectangular sampling grids without generating samples yet.

- [ ] Create `rf-scene` depending only on `rf-domain`.
- [ ] Define bounds and resolution or spacing conventions.
- [ ] Decide whether bounds represent points or cells and whether boundaries are included.
- [ ] Validate reversed, empty, and invalid bounds and resolutions.
- [ ] Test valid and invalid definitions.

**Not yet:** Point generation, rendering, or RF calculations.

**Done when:** Grid definitions have unambiguous invariants and passing boundary tests.

### Milestone 5 — Deterministic grid generation

**Goal:** Generate sampling positions in a documented, repeatable order.

- [ ] Create `rf-simulation` depending on `rf-domain` and `rf-scene`.
- [ ] Generate positions from a valid grid definition without combining generation and result storage.
- [ ] Document ordering, floating-point boundary behavior, and boundary inclusion rules.
- [ ] Test sample counts, coordinates, ordering, and non-square grids.
- [ ] Check behavior at minimum and large practical resolutions.

**Not yet:** Rendering or propagation calculations.

**Done when:** Identical definitions always produce identical ordered samples.

### Milestone 6 — Source model and propagation contract

**Goal:** Define the inputs and output shape for one educational propagation calculation.

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
- [ ] Define an immutable simulation-result snapshot with immutable collections.
- [ ] Preserve a clear ordered relationship between positions and sampled values.
- [ ] Test empty inputs, repeatability, ordering, and representative results.

**Not yet:** Color mapping, JavaFX rendering, or multiple sources.

**Done when:** Identical inputs produce identical immutable result snapshots without JavaFX.

### Milestone 9 — Visualization ranges and normalization

**Goal:** Convert simulation values into toolkit-independent presentation values.

- [ ] Create `rf-visualization` without JavaFX dependencies.
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
- [ ] Keep calculations and normalization out of rendering code.
- [ ] Manually compare the drawing with a known result.

**Not yet:** Resizing, background execution, editing, or polished styling.

**Done when:** A known result, markers, and legend render correctly without changing core-layer dependencies.

### Milestone 11 — Resizing and background execution

**Goal:** Keep the JavaFX interface responsive while displaying recalculated results.
@@ -288,51 +313,52 @@ Each milestone should produce one small, understandable result. Complete its TOD
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

- [ ] Create `rf-application` depending on stable lower-level modules and without JavaFX dependencies.
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

## 9. Suggested next step for the owner

Complete **Milestone 0.5 personally**. Convert the existing single-module build into a parent reactor with one real child module, `rf-desktop`, while preserving the current build behavior. Understand the difference between Maven reactor modules and optional JPMS descriptors before deciding whether JPMS is useful later.

Keep this milestone limited to build structure and source movement. Do not create empty future modules or begin RF behavior. Run the complete root build yourself, and do not begin Milestone 1 until the Milestone 0.5 completion statement is true.
