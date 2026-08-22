# Imperium Enterprise Refactor Plan

> Status: implemented on `codex/work`. See `REFACTOR_REPORT.md` for the delivered outcome and
> `ARCHITECTURE.md` for the resulting runtime architecture.

## 1. Executive summary

Imperium currently builds and runs, but its behavior is distributed across UI code, Room callbacks,
`Application`, static content objects, repositories, and five duplicated feature implementations. The
refactor will preserve the product experience while replacing the current multiple-sources-of-truth
design with a layered, testable architecture.

The target follows the current Android architecture guidance:

- a data layer that owns persistence and content sources;
- a domain layer for reusable game rules and use cases;
- a presentation layer built around immutable UI state and unidirectional data flow;
- Hilt-managed dependencies and lifecycle-aware `Flow` collection;
- a single parameterized implementation for levels, quizzes, and results;
- Room as the source of truth for progress and attempt history;
- deterministic unit tests for every game rule and repository transaction.

## 2. Baseline

Measured on branch `codex/work` before refactoring:

- 94 Kotlin source files and 21,014 Kotlin lines;
- 34 files containing Compose UI;
- 13 Hilt ViewModels;
- several screens between 500 and 900 lines;
- one trivial unit test and one template instrumentation test;
- `testDebugUnitTest`, `lintDebug`, and `assembleDebug` pass;
- the Kotlin compiler reports more than 20 dead-code or unused-symbol warnings;
- dependencies and the Android build toolchain are multiple major release lines behind.

This successful build is the behavioral baseline. Every migration phase must keep these verification
tasks green.

## 3. Principal findings

### P0 — Data integrity

1. Database initialization has two competing owners. `ImperiumDatabase.Callback.onCreate()` and
   `ImperiumApplication.onCreate()` seed overlapping records asynchronously from different catalogs.
   Their category metadata and level counts disagree.
2. Room uses `fallbackToDestructiveMigration()`, so a schema version change can silently erase user
   progress.
3. Progress saving is not atomic. Level progress, category progress, unlocking, and star totals are
   written through separate calls.
4. `totalStarsEarned` adds all stars on every replay instead of adding only the improvement over the
   previous best. Replaying a completed level inflates totals.
5. Result screens repeat progress writes and unlocking logic. Navigation/recomposition can therefore
   make persistence behavior difficult to reason about.
6. There is no attempt-history table. The progress dashboard fabricates historical chart variation
   from the current aggregate score and calendar date.

### P0 — Gameplay correctness

1. Quiz state and timer state live directly in composables, so process recreation and navigation can
   reset or duplicate game state.
2. Five quiz implementations use almost identical state machines and UI, with small behavioral
   differences such as timer defaults.
3. Scoring and star rules exist in both `QuizUtils` and `QuizRepository`, with different APIs and
   thresholds expressed in different units.
4. Empty or unknown level content is handled inconsistently: some catalogs return an empty list and
   others silently return level 1.
5. Results are transported as six mutable path arguments rather than represented as one saved game
   result.

### P1 — Architecture

1. UI code directly reaches into `ImperiumApplication.repository` and Room-derived entities.
2. The data layer imports Compose icons, colors, and UI models, reversing the intended dependency
   direction.
3. DAOs contain business rules and wall-clock access, making them hard to test and reuse.
4. Manual ViewModel factories and Hilt ViewModels coexist.
5. Static content, Room entities, domain models, UI models, and API DTOs are not clearly separated.
6. Navigation contains generic routes plus five era-specific route families for the same features.

### P1 — Background work and networking

1. Badge refresh performs a network request on every app start and also schedules periodic work.
2. `BadgeWorkManager.isPeriodicWorkScheduled()` blocks on a `ListenableFuture.get()` call.
3. The worker contains an artificial two-second delay and caches only partial badge metadata in raw
   `SharedPreferences`.
4. HTTP BODY logging is enabled for every build type.
5. Network responses use repeated `body()!!` access and broad exception handling.

### P1 — Maintainability and quality

1. Level, quiz, result, and result-ViewModel implementations are duplicated per era.
2. The same catalog metadata is repeated in level files, home/category UI, database seed code, and
   navigation branches.
3. Large composables combine state, persistence, navigation, animation, theme, and rendering.
4. Comments describe temporary fixes and debug paths that became production code.
5. Room schema export is disabled and there are no migration tests.

## 4. Target architecture

```text
app
├── core
│   ├── designsystem       Compose theme and reusable components
│   ├── model              shared immutable value objects
│   ├── database           Room database, entities, DAOs, migrations
│   ├── network            Retrofit services and DTOs
│   └── common             dispatcher, clock, result/error utilities
├── data
│   ├── content            local history catalog data source
│   ├── progress           progress/attempt repository implementation
│   └── badges             remote/cache repository implementation
├── domain
│   ├── model              Category, Level, Question, Attempt, Progress
│   ├── repository         interfaces owned by the domain
│   └── usecase            load quiz, submit answer/result, unlock, analytics
└── feature
    ├── home
    ├── category
    ├── levels
    ├── quiz
    ├── results
    ├── progress
    └── profile
```

Dependency direction is `feature -> domain <- data`, with framework-specific entities and DTOs kept
behind mappers. Room is the single source of truth for mutable user data; the packaged content source
is the single source of truth for immutable game content.

## 5. Migration phases

### Phase 1 — Safety and toolchain

- establish real unit-test dependencies and deterministic clock/dispatcher abstractions;
- update the build to Java 17 and a supported stable Android/Kotlin/Compose toolchain;
- centralize every version in the version catalog;
- enable Room schema export and release shrinking;
- add CI-friendly verification tasks.

### Phase 2 — Domain and data integrity

- introduce typed IDs and immutable domain models;
- centralize scoring, star, unlocking, and timer policies;
- add a quiz-attempt entity and DAO queries for real analytics;
- replace destructive migration with an explicit migration;
- implement one transactional `recordQuizAttempt` write;
- consolidate seeding under one idempotent initializer.

### Phase 3 — Unified game engine

- introduce one `QuizViewModel` with immutable `QuizUiState` and explicit events;
- move timer ownership from composables to the state holder;
- use one quiz screen for every era;
- persist completion once and navigate with a typed result identifier;
- cover scoring, selection, timeout, retry, and completion with unit tests.

### Phase 4 — Unified catalog experience

- use one category catalog and one level model;
- replace five level screens with one parameterized feature;
- replace five results screens/ViewModels with one feature;
- collapse era-specific navigation routes into typed routes;
- remove obsolete duplicated files and manual factories.

### Phase 5 — Progress, badges, and settings

- compute progress charts only from recorded attempts;
- isolate UI display models from repository models;
- replace blocking WorkManager inspection with `Flow` APIs;
- cache complete badge data through a repository-owned local source;
- make network logging debug-only and model failures explicitly;
- replace ad-hoc preference access with a settings data source.

### Phase 6 — UI decomposition and hardening

- split large screens into stateless, previewable components;
- make layouts adaptive and accessibility-aware;
- move all user-facing strings into resources;
- remove obsolete resources, backup files, debug logging, and stale reports;
- add repository, migration, ViewModel, navigation, and Compose smoke tests.

## 6. Definition of done

- A category is added through one catalog entry, without a new route, screen, ViewModel, or result
  implementation.
- A quiz attempt is persisted exactly once in a Room transaction.
- Replaying a level cannot inflate total stars; only a better star result changes the total.
- Progress charts contain only recorded attempts and expose an honest empty state.
- No production UI or ViewModel accesses a DAO, database, DTO, or `Application` service locator.
- No destructive Room migration path exists.
- No era-specific quiz, level, or results implementation remains.
- Unit tests cover game rules and state transitions; migration and critical UI flows have automated
  checks.
- `testDebugUnitTest`, `lintDebug`, and `assembleDebug` pass with no new warnings.
- Architecture, schema, local setup, and extension points are documented.
