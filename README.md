# RF Simulator

RF Simulator is a planned educational JavaFX desktop application for exploring simplified radio-frequency propagation concepts while learning professional Java software development.

The project is developed incrementally with Java 21, JavaFX, JUnit 5, and a Maven multi-module build.

> **Important:** RF Simulator is intended for education only. It is not a professional electromagnetic solver, certification tool, operational navigation tool, or safety-analysis tool.

## Current Status

The reproducible multi-module Maven foundation, minimal JavaFX application,
first immutable scalar RF values, minimum immutable two-dimensional geometry
vocabulary, and valid rectangular grid definitions are complete. The next
focused step is to generate deterministic sample positions from those grid
definitions.

No RF simulation behavior has been implemented yet.

## Goals

- Learn Java architecture, testing, documentation, and incremental delivery.
- Build understandable and testable educational RF models.
- Keep physical units, assumptions, and model limitations explicit.
- Keep simulation behavior independent of JavaFX presentation code.
- Use Maven modules to enforce stable dependency boundaries as responsibilities are introduced.
- Practice clean, simple, test-driven Java development using KISS, YAGNI,
  responsible DRY, and SOLID principles where they improve the current design.

## Intended Maven Modules

Modules are introduced only when an active milestone gives them real behavior. Empty future modules are not created in advance.

| Module | Responsibility | Introduced when |
| --- | --- | --- |
| `rf-desktop` | JavaFX lifecycle, controls, events, and drawing | Multi-module foundation |
| `rf-domain` | Immutable units, values, geometry, and invariants | Units milestone |
| `rf-scene` | Toolkit-independent scenario descriptions | Grid/scene concepts require it |
| `rf-simulation` | Deterministic propagation calculations and sampling | Calculation behavior begins |
| `rf-visualization` | Toolkit-independent normalization and display preparation | Visualization preparation begins |
| `rf-application` | Commands, use cases, and mutable-state ownership | Application workflows begin |
| `rf-persistence` | Versioned scenario storage, only if justified | Persistence milestone |

Only `rf-desktop` may depend on JavaFX. Maven modules are used first; Java Platform Module System descriptors (`module-info.java`) remain optional and require a separate documented reason.

## Documentation

- [`PROJECT_PLAN.md`](PROJECT_PLAN.md) defines the development rules, milestone order, completion criteria, and non-goals.
- [`DESIGN_GUIDE.md`](DESIGN_GUIDE.md) defines module boundaries, dependency direction, responsibility placement, and design review rules.
- [`AGENT.md`](AGENT.md) defines collaboration rules for AI assistance.

## Building and Testing

Java 21 is required. Run commands from the repository root so Maven builds the complete active reactor.

Run tests:

```bash
./mvnw test
```

Verify the complete build:

```bash
./mvnw verify
```

After multiple modules exist, run one module and all dependencies required to build it with:

```bash
./mvnw -pl <module-name> -am test
```

Run the desktop application:

```bash
./mvnw -pl rf-desktop javafx:run
```

On Windows Command Prompt or PowerShell, use `mvnw.cmd` instead of `./mvnw`.

## License

This project is licensed under the [MIT License](LICENSE).
