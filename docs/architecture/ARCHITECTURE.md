# Imperium Architecture

## Goals

Imperium is structured around three boundaries:

1. Presentation renders immutable state and forwards user events.
2. Domain code owns game rules and repository contracts without Android dependencies.
3. Data implementations own persistence, packaged content, and remote badge access.

This keeps gameplay deterministic, makes data integrity enforceable in one place, and lets every
history category use the same feature code.

## Runtime data flow

```text
Navigation route
    |
    v
Compose screen <---- immutable UI state ---- ViewModel
    |                                       |
    +--------------- user event ---------->|
                                            v
                                 Domain repository contract
                                            ^
                                            |
                           +----------------+----------------+
                           |                                 |
                local content catalog                 Room repositories
                (immutable questions)          (attempts and progress)
```

Badge synchronization is a separate boundary:

```text
unique periodic WorkManager request -> Hilt worker -> BadgeRepository -> Retrofit
```

## Package responsibilities

### `domain/model`

Framework-independent value objects such as `CategoryId`, `HistoryCategory`, `HistoryLevel`,
`QuizQuestion`, `QuizAttempt`, and `QuizResult`. Constructors validate invariants so invalid scores,
indexes, durations, and identifiers cannot enter the core model unnoticed.

### `domain/game`

`GameRules` is the single owner of scoring, star thresholds, star deltas, unlock policy, and default
quiz duration. `QuizSession` is an immutable state machine for answer selection, submission, timer
ticks, question advancement, and completion.

### `domain/repository`

Contracts describe the capabilities needed by features:

- `HistoryContentRepository` reads category, level, and question content.
- `GameProgressRepository` observes progress and atomically records attempts.
- `ProgressAnalyticsRepository` exposes attempt-derived dashboard statistics.

The domain layer does not know whether a contract is backed by Room, a packaged catalog, or a remote
service.

### `data/repository`

- `LocalHistoryContentRepository` is the one catalog for packaged game content. Only levels that
  have questions are exposed.
- `RoomGameProgressRepository` records an attempt and updates best-level/category progress inside one
  Room transaction.
- `ProgressRepository` calculates daily points, streaks, improvement, and category performance from
  immutable attempt rows.

### `feature`

The `levels`, `quiz`, and `results` packages are category-independent. Each route carries only a
stable category ID, level number, or attempt ID. The result route never transports mutable game
totals as path arguments.

The existing `ui/home`, `ui/category`, `ui/progress`, and `ui/profile` packages use the same domain
contracts. They are retained as feature packages until a future physical multi-module split is
justified by build-time or team-ownership constraints.

### `data/database`

Room stores mutable user data. Schema version 4 adds immutable `quiz_attempts`; schemas are exported
to `app/schemas`. Legacy content tables remain in the schema so existing installations can migrate
without losing data, but runtime quiz content is read only from the packaged catalog.

## Persistence invariants

`recordQuizAttempt` is the only write path for a completed quiz and guarantees:

- an immutable attempt row is inserted exactly once;
- the level keeps the best score, percentage, and star result;
- category stars equal the sum of level bests, not the sum of every replay;
- the next level is unlocked according to `GameRules`;
- parent category progress exists before child progress is written.

The database has no destructive migration fallback.

## State ownership

`QuizViewModel` owns the active session and timer job. Compose owns visual-only state such as local
animation progress. A session survives ordinary recomposition and produces a single navigation
event only after persistence returns an attempt ID.

`StateFlow` is used for durable screen state. One-time navigation is delivered separately so screen
recomposition cannot save progress again.

## Dependency injection

Hilt creates the database, DAOs, Retrofit client, repositories, ViewModels, sound manager, and worker
factory. Domain contracts are bound to their data implementations in `RepositoryModule`.

Production code must not introduce an application service locator or instantiate repositories in a
screen/ViewModel.

## Background work and networking

Badge sync is scheduled as a unique 12-hour periodic request with `ExistingPeriodicWorkPolicy.UPDATE`.
The app does not block on WorkManager futures and does not force a duplicate request on every launch.
The worker retries I/O and server failures, while non-retriable HTTP failures are surfaced as failure.
OkHttp uses BASIC logging only for debug builds and disables logging in release builds.

## Extension rules

When adding a category or level:

1. Add its immutable content data.
2. Register it in `LocalHistoryContentRepository` with a stable ID.
3. Add tests asserting question/level availability.

Do not add era-specific screens, ViewModels, navigation destinations, scoring functions, or Room
seed paths.

When changing a schema:

1. Increment the Room version.
2. Add an explicit migration.
3. Commit the generated schema JSON.
4. Add or update a migration/integration test.

When changing game rules, modify `GameRules` first and update its unit tests before adapting UI copy.
