# Enterprise Refactor Report

## Outcome

The refactor replaced Imperium's duplicated era-specific application paths with one layered game
architecture while preserving the five-category product. The active source was reduced from 94
Kotlin files / 21,014 lines to 69 Kotlin files / 8,190 lines. This is a reduction of 25 files and
12,824 lines (61%) before generated code and tests.

## Delivered changes

### Build and platform

- Upgraded the project to AGP 9.3, Gradle 9.7, Kotlin 2.4, Java 17, API 37, and the current Compose
  BOM used by the project.
- Adopted built-in Kotlin support, the Compose compiler plugin, and KSP.
- Centralized versions in the Gradle version catalog.
- Enabled release minification/resource shrinking and Room schema export.
- Removed obsolete Firebase Performance, MPAndroidChart, JitPack, and unused runtime dependencies.

### Domain and game engine

- Introduced validated, immutable category, level, question, result, and attempt models.
- Centralized scoring, star thresholds, unlocks, and star-delta calculations.
- Added an immutable quiz session state machine with ViewModel-owned timing.
- Replaced five level, five quiz, and five result implementations with one parameterized flow.

### Data integrity

- Removed competing asynchronous database seed paths.
- Added immutable quiz-attempt history and an explicit schema 3-to-4 migration.
- Removed destructive migration behavior.
- Made attempt, personal-best, star, category, and unlock writes one Room transaction.
- Fixed replay star inflation by deriving totals from per-level bests.
- Replaced generated dashboard variation with analytics based on actual attempts.

### Presentation

- Changed result navigation from six mutable scalar arguments to one persisted attempt ID.
- Removed DAO/application service-locator access from production UI.
- Rebuilt the progress visualization in Compose and removed the Android View chart bridge.
- Modernized Material 3 theming, dynamic color, and edge-to-edge activity setup.
- Replaced hard-coded profile statistics with repository-backed attempt data.

### Background work and network

- Adopted the official Hilt WorkManager factory integration.
- Changed badge synchronization to unique periodic work without blocking future access.
- Removed artificial worker delay, duplicate startup fetches, unsafe response unwrapping, and release
  HTTP body logging.
- Centralized badge fetching in one repository and guarded overlapping refreshes.

### Tests

- Added unit coverage for `GameRules`, `QuizSession`, the local content catalog, and analytics.
- Added an in-memory Room instrumentation test for atomic attempt recording and replay-safe stars.
- Added CI quality gates for tests, Android test compilation, lint, and an optimized release build.

## Correctness decisions

- The catalog exposes only content that really exists. Ancient History currently has eight playable
  levels; the other four categories have twenty each.
- Legacy content tables remain in Room solely to preserve the version-3 migration path. They are not
  a second runtime source of quiz content.
- Mutable progress is local-first. Badge data remains remote because the existing product contract
  supplies it through an API.

## Verification

Local verification commands:

```bash
./gradlew testDebugUnitTest compileDebugAndroidTestKotlin lintDebug assembleDebug
./gradlew assembleRelease
```

Instrumented Room tests compile on every CI run and can be executed on a connected API 33+ device
with `./gradlew connectedDebugAndroidTest`.

## Follow-up boundary

A physical Gradle multi-module split was intentionally not introduced. The dependency boundaries are
now explicit in packages and interfaces; splitting them into modules should be driven by team
ownership or measurable build-time pressure, not used as a substitute for architecture.
