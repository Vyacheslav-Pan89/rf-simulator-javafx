# RF Simulator

RF Simulator is a planned educational JavaFX desktop application for exploring simplified radio-frequency propagation concepts while learning professional Java software development.

The project will be developed incrementally using Java 21, JavaFX, Maven, and JUnit 5.

> **Important:** RF Simulator is intended for education only. It is not a professional electromagnetic solver, certification tool, or safety-analysis tool.

## Current Status

The Maven and test foundation is complete. No application or simulation functionality has been implemented yet.

## Goals

- Learn Java architecture, testing, documentation, and incremental development.
- Build understandable and testable educational RF models.
- Keep physical units, assumptions, and model limitations explicit.
- Separate simulation behavior from JavaFX presentation code.

## Project Plan

See [`PROJECT_PLAN.md`](PROJECT_PLAN.md) for:

- the planned architecture;
- the target design and responsibility boundaries in [`DESIGN_GUIDE.md`](DESIGN_GUIDE.md);
- development rules;
- testing and RF-documentation guidance;
- the milestone roadmap;
- completion criteria; and
- the suggested first implementation step.

## Building and Testing

Java 21 is required.

Run tests:

```bash
./mvnw test
```

Verify the complete build:

```bash
./mvnw verify
```

On Windows Command Prompt or PowerShell, use `mvnw.cmd` instead of `./mvnw`.

## License

This project is licensed under the [MIT License](LICENSE).
