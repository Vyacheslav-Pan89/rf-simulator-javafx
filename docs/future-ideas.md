# Future Ideas

These notes capture possible future directions for RF Simulator. They are not
active project scope until a milestone explicitly adopts them.

Use this file to preserve ideas without expanding the current milestone.

## Propagation And Environment

- Free-space propagation mode.
- Free-space path loss model.
- Ground-aware propagation mode.
- Two-ray ground reflection model.
- Log-distance path loss model.
- Simple indoor attenuation model.
- Simple obstacle attenuation.
- Simple terrain shadowing.
- Source altitude.
- Receiver or sample altitude, if later required.
- Terrain or ground-surface model.
- Flat-earth approximation mode.
- Curved-earth approximation mode, only if educationally useful.
- Simplified ground-effect calculations.
- Simplified refraction calculations.
- Environmental presets such as open field, urban, indoor, or airport-like
  training scenario.
- Material categories for simplified attenuation, such as concrete, glass,
  metal, vegetation, and terrain.
- Weather or atmosphere placeholder metadata, only if a documented model later
  needs it.
- Fresnel-zone educational overlay.
- Line-of-sight and blocked-line-of-sight classification.
- Near-source singularity handling options.
- Model comparison mode.
- Model metadata describing equations, assumptions, valid range, and
  limitations.

## Sources, Receivers, And Antennas

- Multiple sources.
- Receiver or probe point.
- Source labels or names.
- Source frequency.
- Source transmit power.
- Source altitude.
- Receiver altitude.
- Source enable/disable toggle.
- Source color or marker style.
- Directional antenna pattern.
- Antenna gain.
- Antenna orientation.
- Simple omnidirectional antenna preset.
- Simple sector antenna preset.
- Imported antenna pattern file, only if a stable format is justified.
- Multiple receiver probes.
- Probe history or comparison between probes.
- Snap source or receiver positions to grid.

## UI And Visualization

- Toggle between free-space and ground-aware calculation modes.
- Display source altitude and environmental assumptions.
- Display selected propagation model limitations near results.
- Visualize terrain or ground effects only after the underlying model is
  documented and tested.
- Heatmap color scale.
- Legend with units.
- Source markers.
- Receiver or probe markers.
- Grid bounds overlay.
- Grid resolution display.
- Coordinate readout under cursor.
- Probe tool: click a point and show calculated value.
- Tooltip or side panel for selected sample details.
- Contour lines.
- Threshold coverage overlay.
- Toggle between linear and logarithmic display where meaningful.
- Display minimum, maximum, average, and selected value.
- Side-by-side model comparison view.
- Difference heatmap between two models.
- Opacity controls for overlays.
- Export image of current visualization.
- Export simple report with model, assumptions, inputs, and result summary.
- Reset view and zoom/pan controls.
- Fit-to-grid view.
- Dark/light theme.
- Keyboard shortcuts after workflows stabilize.
- Display formula used by selected model.
- Display a step-by-step calculation explanation for a selected point.
- Display why a value changed after a parameter edit.
- Show warning banner that results are educational and not operational.

## Scenario And Persistence

- Save and load scenarios.
- Versioned scenario file format.
- Example demo scenarios.
- Scenario validation report.
- Recent files list.
- Import/export JSON scenario documents.
- Scenario templates such as open field, simple indoor, obstacle demo, and
  radio-navigation lesson.
- Autosave or recovery file, only if application workflows become complex.
- Human-readable scenario diff or summary.
- Stable migration path for old scenario versions.
- Export calculated samples to CSV.
- Export metadata and assumptions with saved results.

## Grid And Sampling

- Deterministic point generation from grid definitions.
- Different grid resolutions for x and y axes.
- Preview estimated sample count before calculation.
- Grid spacing display derived from bounds and sample counts.
- Large-grid warnings.
- Maximum practical resolution limits.
- Sampling order documentation and tests.
- Optional sparse sampling mode.
- Optional adaptive sampling mode, only if a later model needs it.
- Separate grid definition, point generation, and sampled result storage.
- Immutable simulation result snapshots.

## Learning Features

- Formula panel for selected model.
- Assumptions and limitations panel.
- Reference examples with known expected values.
- Step-by-step explanation for one selected sample.
- Compare two models side by side.
- Small glossary of RF terms used by the simulator.
- Links from UI labels to documentation pages.
- Guided tutorial scenarios.
- Explain units and conversions near user inputs.
- Show why logarithmic and linear quantities must not be mixed silently.
- Educational notes for dBm, watts, path loss, gain, and attenuation.

## Engineering And Quality

- Architecture boundary tests.
- Checks that JavaFX dependencies stay out of domain, scene, simulation, and
  visualization modules.
- Golden reference cases for propagation models.
- Deterministic regression snapshots for small grids.
- Performance checks for large grids.
- Background calculation cancellation.
- Progress reporting for expensive calculations.
- Immutable result snapshots passed to presentation code.
- Explicit error-handling strategy for UI input validation.
- Centralized user-facing error formatting in the application or UI layer.
- Logging strategy for unexpected failures.
- Test utilities for floating-point tolerances.
- Benchmark or profiling notes before optimization.
- Release checklist.
- Changelog when tagged releases begin.

## Possible Radio-Navigation Lessons

- ILS localizer concept demonstration.
- ILS glide slope concept demonstration.
- VOR radial concept demonstration.
- DME distance concept demonstration.
- Compare idealized radio-navigation behavior with simplified propagation
  effects.
- Prominent non-operational disclaimer for every navigation lesson.

## Current Constraints

- Milestone 4 grid definition remains two-dimensional.
- `RectangularGridDefinition` should not include altitude, terrain, or
  propagation behavior.
- Altitude and environment concepts should wait until a later scene or
  propagation milestone gives them clear responsibilities.
