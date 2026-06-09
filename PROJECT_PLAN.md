# RF Simulator JavaFX Project Plan

This document is the working plan for the repository owner. It describes what to learn, decide, implement, document, and validate without beginning the implementation. Work through it incrementally, and keep every implementation item unchecked until the owner personally completes and verifies it.

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

The owner should plan around the following choices while keeping setup work in Milestone 0:

- Java 21.
- JavaFX for the desktop presentation layer, initially using programmatic UI construction.
- Maven with the Maven Wrapper so builds do not depend on a globally installed Maven version.
- JUnit 5 for automated tests.
- GitHub Actions for continuous integration.
- Base package `com.rfsimulator`.
- One Maven module initially; split modules only if a documented need appears later.
- No Spring or dependency-injection framework. Prefer explicit constructors and straightforward object creation.
- No FXML initially unless the owner later identifies and records a clear benefit.
- No `module-info.java` initially unless the owner later identifies and records a clear reason.

Possible future Maven setup items include JavaFX Controls, JavaFX Maven Plugin, Maven Compiler Plugin, Maven Surefire Plugin, Maven Enforcer Plugin, and JUnit Jupiter. The owner should research current compatible versions, understand each tool's purpose, and add only what the current milestone needs.

## 3. Architectural layers and dependency rules

The conceptual layers are:

```text
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

1. Work on one milestone at a time. Do not pull later-milestone ideas into the current implementation.
2. Before starting a milestone, restate its purpose, included work, excluded work, and completion criteria.
3. Learn the smallest relevant concept, implement a small slice, test it, and then continue.
4. Write tests alongside domain and simulation behavior rather than postponing them.
5. Use Maven Wrapper commands instead of relying on a global Maven installation.
6. Run `./mvnw test` frequently during development and `./mvnw verify` before finishing a milestone.
7. Run `./mvnw javafx:run` when manually validating the application. Treat launch failures caused solely by a headless environment as environment limitations, while still investigating ordinary application failures.
8. Keep commits small, focused, and explainable. A commit should teach or deliver one coherent idea.
9. Record important architecture and RF-model decisions using the decision log template in this document.
10. Update this plan only when scope or decisions genuinely change; do not mark aspirational work complete.
11. Review the purpose, maintenance cost, license, and alternatives before adding a dependency.
12. Preserve unit clarity and numerical determinism across refactoring.
13. Profile before optimizing. Avoid premature concurrency, advanced rendering, generalized frameworks, and speculative abstraction.

## 6. Testing strategy

Testing should concentrate below the JavaFX layer, where behavior is fast and deterministic to verify.

- Write unit tests for values, formulas, grids, validation rules, and model boundaries.
- Use deterministic reference cases for simulation output. Given identical inputs and configuration, results should be identical within a documented floating-point tolerance.
- Choose tolerances deliberately for floating-point comparisons. Explain whether a test uses absolute tolerance, relative tolerance, or both, and why.
- Add architecture checks when useful to prevent JavaFX dependencies from entering domain, simulation, or scene packages.
- Add integration tests for application workflows only when unit tests cannot give sufficient confidence.
- Keep UI testing minimal. Test calculations, normalization, and workflow decisions beneath JavaFX, and manually validate essential rendering behavior.
- Include explicit edge cases where relevant: zero and negative values, invalid or reversed bounds, unsupported units, empty scenes, empty grids, extreme resolutions, and sample points at or near a source.
- Test failures and validation messages, not only successful paths.
- Keep tests readable as examples of intended behavior and RF assumptions.

## 7. RF model documentation rules

Before completing any RF model, document all of the following near its design or public contract:

- The equation in readable notation.
- Every input and output unit.
- Assumptions made by the model.
- Valid operating range and conditions.
- Singularity and boundary handling, including behavior at or near a source.
- Expected qualitative behavior, such as whether output should decrease with distance.
- Known limitations and effects deliberately omitted.
- One or more reference cases used by automated tests, including the source of expected values.

Use unambiguous names such as `frequencyHz`, `distanceMeters`, `powerWatts`, and `fieldStrengthDbm` where practical. Never mix linear and logarithmic quantities silently: conversions between watts, milliwatts, dBW, dBm, ratios, and decibels must be explicit, named, documented, and tested. Keep the educational disclaimer visible wherever model results could be misunderstood.

## 8. Milestone roadmap with unchecked TODOs

### Milestone 0 — Project foundation

**Purpose:** Learn and establish a reproducible Java 21 desktop-project workflow before adding RF concepts.

**Included work:**

- [ ] Research and select compatible Java 21, JavaFX, Maven plugin, and JUnit 5 versions.
- [ ] Configure one Maven module and the `com.rfsimulator` base package.
- [ ] Add and verify the Maven Wrapper.
- [ ] Configure JavaFX Controls and a minimal launchable window without final styling.
- [ ] Configure JUnit 5 and write one meaningful non-UI test.
- [ ] Configure GitHub Actions to run the Maven verification lifecycle.
- [ ] Document local build, test, run, and CI commands.

**Excluded work:** RF behavior, grids, final UI design, persistence, and 3D rendering.

**Completion criteria:**

- [ ] A fresh checkout can use the Maven Wrapper with Java 21 to build and test.
- [ ] The minimal JavaFX window launches in a graphical environment.
- [ ] At least one non-UI test passes locally and in GitHub Actions.
- [ ] No RF, grid, persistence, or advanced visualization behavior has been introduced.

**Suggested validation checks:**

- [ ] Run `./mvnw test`.
- [ ] Run `./mvnw verify`.
- [ ] Run `./mvnw javafx:run` in a graphical environment.
- [ ] Review the dependency tree and confirm every dependency has a current purpose.
- [ ] Confirm CI uses Java 21 and the Maven Wrapper.

### Milestone 1 — Domain foundation and mathematical values

**Purpose:** Build a clear, immutable vocabulary for later RF and geometry work.

**Included work:**

- [ ] Define unit conventions and naming rules before introducing calculations.
- [ ] Design immutable vectors, positions, distances, frequency, and power values.
- [ ] Decide where Java records improve clarity and where validated classes are more suitable.
- [ ] Define and test equality, conversion, validation, and boundary behavior.
- [ ] Keep every type independent of JavaFX.

**Excluded work:** grids, propagation formulas, rendering, scenarios, and UI controls.

**Completion criteria:**

- [ ] Each value type has documented units, invariants, and invalid-input behavior.
- [ ] Boundary, equality, and conversion tests pass deterministically.
- [ ] Domain source and tests have no JavaFX dependency.

**Suggested validation checks:**

- [ ] Run `./mvnw test` and `./mvnw verify`.
- [ ] Review public names for explicit units.
- [ ] Check zero, negative, non-finite, and equality edge cases as applicable.

### Milestone 2 — Grid system

**Purpose:** Represent deterministic sampling locations without introducing RF calculations or rendering.

**Included work:**

- [ ] Define grid bounds, resolution, points or cells, and ordering conventions.
- [ ] Validate bounds and resolution before generation.
- [ ] Generate grid samples deterministically.
- [ ] Test counts, coordinates, ordering, boundaries, and invalid input.

**Excluded work:** rendering, heatmaps, propagation calculations, and advanced RF behavior.

**Completion criteria:**

- [ ] The same valid grid definition always produces the same samples and ordering.
- [ ] Invalid bounds and resolutions fail clearly.
- [ ] Grid behavior is fully testable without JavaFX.

**Suggested validation checks:**

- [ ] Test minimum valid grids, non-square grids, reversed bounds, and extreme resolutions.
- [ ] Verify documented inclusion or exclusion of maximum boundaries.
- [ ] Run `./mvnw verify`.

### Milestone 3 — Basic simulation engine

**Purpose:** Connect domain values and grid samples through one understandable educational propagation model.

**Included work:**

- [ ] Define a minimal source model with explicit units.
- [ ] Select and document one educational propagation model using the RF documentation rules.
- [ ] Sample the model over grid points.
- [ ] Produce immutable simulation-result snapshots.
- [ ] Add deterministic reference and boundary tests.
- [ ] State prominently that simplified output is not a professional-grade RF prediction.

**Excluded work:** multiple models, obstacles, terrain, antenna patterns, final visualization, and professional prediction claims.

**Completion criteria:**

- [ ] The model equation, assumptions, units, valid range, and limitations are documented.
- [ ] Reference cases and singularity behavior are tested.
- [ ] Identical inputs produce identical immutable results.
- [ ] Simulation tests run without JavaFX.

**Suggested validation checks:**

- [ ] Compare test results with independently calculated reference cases.
- [ ] Test points at, near, and far from a source according to documented limits.
- [ ] Run `./mvnw verify` and review numerical tolerances.

### Milestone 4 — Toolkit-independent visualization model

**Purpose:** Convert simulation output into presentation-ready values without coupling calculations to JavaFX.

**Included work:**

- [ ] Define value ranges and normalization policies.
- [ ] Define legend labels and toolkit-independent color-map values.
- [ ] Convert immutable simulation output into immutable presentation-ready snapshots.
- [ ] Test empty, constant, minimum, maximum, clipped, and invalid ranges.

**Excluded work:** JavaFX Canvas drawing, controls, animation, and 3D rendering.

**Completion criteria:**

- [ ] Visualization-model code has no JavaFX dependency.
- [ ] Normalization and legend behavior are documented and deterministically tested.
- [ ] Simulation output remains unchanged during conversion.

**Suggested validation checks:**

- [ ] Test normalization boundaries and constant-value inputs.
- [ ] Confirm values are suitable for more than one possible presentation toolkit.
- [ ] Run `./mvnw verify`.

### Milestone 5 — JavaFX Canvas presentation

**Purpose:** Present existing immutable results in a simple, understandable 2D desktop view.

**Included work:**

- [ ] Draw a 2D heatmap on JavaFX Canvas.
- [ ] Draw source markers and a readable legend.
- [ ] Handle resizing without moving calculations into rendering code.
- [ ] Keep rendering separate from simulation and normalization.
- [ ] Run expensive work in the background and publish immutable snapshots to the JavaFX Application Thread.

**Excluded work:** advanced 3D, polished final design, heavy animation, and new propagation behavior.

**Completion criteria:**

- [ ] The Canvas displays a known result, markers, and legend correctly.
- [ ] Resizing preserves understandable output.
- [ ] Expensive calculation does not block the JavaFX Application Thread.
- [ ] Core layers remain free of JavaFX dependencies.

**Suggested validation checks:**

- [ ] Run `./mvnw javafx:run` and manually inspect resizing and rendering.
- [ ] Validate background-task success, failure, and cancellation behavior where applicable.
- [ ] Run `./mvnw verify`; document any genuinely headless-only UI launch limitation.

### Milestone 6 — Scene model and application commands

**Purpose:** Introduce explicit scenarios and use cases while defining who owns mutable application state.

**Included work:**

- [ ] Model scenarios containing sources and relevant parameters without JavaFX types.
- [ ] Design command or use-case objects for source placement, parameter updates, and simulation reruns.
- [ ] Define one clear owner for mutable application state.
- [ ] Publish immutable snapshots across layer boundaries.
- [ ] Test application workflows below the UI.

**Excluded work:** persistence, multiple advanced propagation models, obstacles, terrain, and navigation systems.

**Completion criteria:**

- [ ] State ownership and update flow are documented and understandable.
- [ ] Commands coordinate behavior without presentation logic.
- [ ] Application workflows have focused tests where needed.

**Suggested validation checks:**

- [ ] Trace a user action from UI intent to command to immutable result.
- [ ] Test empty scenarios, invalid updates, reruns, and repeated commands.
- [ ] Run `./mvnw verify`.

### Milestone 7 — Signal sources and propagation models

**Purpose:** Compare multiple educational source and propagation choices through explicit interchangeable strategies.

**Included work:**

- [ ] Introduce multiple source types only after defining their educational differences.
- [ ] Design interchangeable propagation strategies with explicit inputs and immutable outputs.
- [ ] Define and document source-combination rules, especially linear versus logarithmic handling.
- [ ] Attach metadata describing model assumptions, units, valid ranges, and limitations.
- [ ] Add comparison and reference tests.

**Excluded work:** full-wave simulation, undocumented realism, terrain, and radio-navigation systems.

**Completion criteria:**

- [ ] Strategies can be selected without changing core sampling workflows.
- [ ] Combination rules are mathematically documented and tested.
- [ ] UI can display model metadata and limitations.

**Suggested validation checks:**

- [ ] Compare models with controlled inputs and explain qualitative differences.
- [ ] Verify source ordering does not accidentally change combined results when it should not.
- [ ] Run `./mvnw verify`.

### Milestone 8 — Obstacles, terrain, and antenna patterns

**Purpose:** Add limited environmental and directional effects incrementally while preserving honest model boundaries.

**Included work:**

- [ ] Add one simplified attenuation effect at a time.
- [ ] Model obstacles and terrain with explicit geometry and assumptions.
- [ ] Model antenna gain patterns with explicit angle and gain units.
- [ ] Document interaction rules and limitations for every added effect.
- [ ] Add isolated and combined-effect tests.

**Excluded work:** claims of full-wave electromagnetic simulation, universal material accuracy, and safety analysis.

**Completion criteria:**

- [ ] Every effect can be explained, enabled, and tested independently.
- [ ] Limitations and omitted real-world effects are prominent.
- [ ] Combined behavior remains deterministic and documented.

**Suggested validation checks:**

- [ ] Compare scenes with each effect disabled and enabled.
- [ ] Test geometric boundaries and antenna-pattern interpolation edges.
- [ ] Run `./mvnw verify`.

### Milestone 9 — Radio-navigation systems

**Purpose:** Apply stable core propagation concepts to educational ILS, VOR, and DME scenarios.

**Included work:**

- [ ] Research and document the real-world concepts behind ILS, VOR, and DME.
- [ ] Define the educational question each simulated scenario should teach.
- [ ] Document every simplification and difference from operational systems.
- [ ] Add navigation scenarios incrementally, with reference and qualitative tests.

**Excluded work:** operational guidance, certification, flight-safety decisions, and implementation before core propagation concepts are stable.

**Completion criteria:**

- [ ] Each navigation feature explains the real concept and simulator simplifications.
- [ ] Scenarios reuse stable lower-level models rather than bypassing architecture.
- [ ] Results are clearly labeled educational and non-operational.

**Suggested validation checks:**

- [ ] Review explanations against reputable technical references.
- [ ] Test expected qualitative behavior and documented boundaries.
- [ ] Run `./mvnw verify`.

### Milestone 10 — Scenario persistence and engineering tools

**Purpose:** Make educational scenarios repeatable and add analysis tools after model behavior is stable.

**Included work:**

- [ ] Design versioned scenario serialization.
- [ ] Validate imports and provide clear errors for unsupported or invalid data.
- [ ] Add export workflows that preserve units and model metadata.
- [ ] Add coverage metrics, probes, charts, and engineering-oriented comparison tools incrementally.
- [ ] Test versioning, round trips, invalid input, and analysis calculations.

**Excluded work:** silently accepting ambiguous data, unversioned formats, and claims that analysis output is suitable for certification or safety decisions.

**Completion criteria:**

- [ ] Supported scenarios round-trip without losing defined meaning.
- [ ] Invalid and incompatible inputs fail safely and clearly.
- [ ] Analysis tools state units, model source, assumptions, and limitations.

**Suggested validation checks:**

- [ ] Test empty, minimum, representative, malformed, and unsupported-version scenarios.
- [ ] Verify exported data includes enough metadata to interpret results.
- [ ] Run `./mvnw verify`.

## 9. Project-wide unchecked TODO checklist

- [ ] Keep the educational purpose and non-professional disclaimer visible in documentation and relevant output.
- [ ] Keep domain, simulation, scene, and toolkit-independent visualization code free of JavaFX dependencies.
- [ ] Make units explicit in names, contracts, documentation, and tests.
- [ ] Document every RF model using the rules in this plan.
- [ ] Keep simulation results immutable and deterministic.
- [ ] Keep long-running work off the JavaFX Application Thread.
- [ ] Test behavior below the UI wherever practical.
- [ ] Review every new dependency before adding it.
- [ ] Record important decisions and meaningful reversals.
- [ ] Keep commits focused and complete only one milestone at a time.
- [ ] Profile before optimizing and avoid premature abstraction, concurrency, and advanced rendering.
- [ ] Revisit FXML, Java modules, extra Maven modules, and frameworks only when a documented problem justifies them.

## 10. Decision log template

Copy this template within this section when an architectural or RF-model decision genuinely needs recording:

```text
Decision ID and title:
Date:
Milestone:
Status: proposed | accepted | superseded

Context and learning question:
Options considered:
Decision and reasoning:
Consequences and trade-offs:
Units, numerical, or RF-model implications:
Validation or evidence:
Conditions that would justify revisiting the decision:
```

## 11. Milestone completion checklist template

Use this template before declaring any milestone complete. All relevant items should be satisfied by the owner; non-applicable items should be explained rather than silently ignored.

- [ ] The milestone purpose and included scope are delivered.
- [ ] Excluded and later-milestone work has not leaked into the implementation.
- [ ] Public concepts, units, assumptions, and limitations are documented.
- [ ] Relevant unit, boundary, deterministic, and workflow tests pass.
- [ ] `./mvnw test` passes.
- [ ] `./mvnw verify` passes.
- [ ] `./mvnw javafx:run` has been manually checked when the milestone affects the UI, or a headless environment limitation has been recorded.
- [ ] Core architecture dependency rules still hold.
- [ ] No expensive work runs on the JavaFX Application Thread.
- [ ] Dependencies added during the milestone have been reviewed.
- [ ] Important decisions have been recorded.
- [ ] Documentation and this plan reflect genuine scope or decision changes.
- [ ] Commits are small, focused, and understandable.

## 12. Suggested first step for the owner

Complete **Milestone 0 personally**. Begin by researching how Java 21, JavaFX Controls, Maven, the Maven Wrapper, JUnit Jupiter, the JavaFX Maven Plugin, compiler and test plugins, Maven Enforcer, and GitHub Actions fit together. Understand why each component is needed before configuring it, and select compatible current versions from their primary documentation.

Then plan the smallest possible foundation: one Maven module, the `com.rfsimulator` base package, a minimal programmatically constructed JavaFX window, one non-UI learning test, Maven Wrapper commands, and a CI verification job. Keep RF concepts, grids, persistence, styling, and 3D work out of this milestone. Validate each small addition yourself with the Milestone 0 checks, record any decisions you make, and do not begin Milestone 1 until the foundation completion criteria are satisfied.
