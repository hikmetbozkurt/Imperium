# Imperium

Imperium is an offline-first Android history quiz. It covers Ancient History, the Medieval Era,
the Renaissance, Modern History, and the World Wars through a level-based game loop with scoring,
stars, unlocks, attempt history, and progress analytics.

![Imperium logo](app/src/main/ic_launcher_icon-playstore.png)

## Highlights

- One reusable levels, quiz, and result flow for every history category.
- A deterministic quiz state machine with a ViewModel-owned timer.
- Transactional Room persistence for attempts, personal bests, stars, and unlocks.
- Progress analytics derived from recorded attempts rather than generated sample data.
- Material 3, edge-to-edge layouts, dynamic color, and Compose-native charts.
- Hilt-managed application, repository, ViewModel, and WorkManager dependencies.
- Debug-only HTTP logging and unique periodic badge synchronization.

## Technology

- Kotlin 2.4 and Java 17
- Jetpack Compose with Material 3
- Room with exported schemas and explicit migrations
- Hilt and Hilt WorkManager
- Navigation Compose
- Kotlin Coroutines and Flow
- Retrofit 3 and OkHttp 5
- Gradle 9.7 and Android Gradle Plugin 9.3

All dependency versions are defined in `gradle/libs.versions.toml`.

## Architecture

```text
Compose screen -> ViewModel -> domain repository contract <- data implementation
                              |                         |
                              +-- game rules           +-- Room / local catalog / network
```

Immutable packaged quiz content has one catalog owner. Room is the source of truth for mutable user
progress and attempt history. The UI consumes immutable state and sends events; it does not access
DAOs, network DTOs, or the application object.

Detailed decisions and extension points are documented in
[`docs/architecture/ARCHITECTURE.md`](docs/architecture/ARCHITECTURE.md). The original findings and
the completed migration are recorded in
[`docs/architecture/REFACTOR_PLAN.md`](docs/architecture/REFACTOR_PLAN.md) and
[`docs/architecture/REFACTOR_REPORT.md`](docs/architecture/REFACTOR_REPORT.md).

## Build

Requirements:

- Android Studio with JDK 17
- Android SDK 37

```bash
git clone https://github.com/hikmetbozkurt/Imperium.git
cd Imperium
./gradlew testDebugUnitTest compileDebugAndroidTestKotlin lintDebug assembleDebug
```

Build the optimized release artifact with:

```bash
./gradlew assembleRelease
```

Generated APKs are written under `app/build/outputs/apk/`.

## Tests and quality gates

The project includes deterministic unit tests for scoring, stars, session transitions, the content
catalog, and analytics. The Room instrumentation suite verifies atomic attempt recording and guards
against replay-based star inflation.

Every pull request runs unit tests, Android test compilation, Android lint, and the release build in
GitHub Actions.

## Adding game content

Add or update the packaged question data, then register its category/level metadata in
`LocalHistoryContentRepository`. A new category does not require another levels screen, quiz screen,
result screen, ViewModel, or navigation route.

## License

Copyright Hikmet Bozkurt Aydoğan. Distributed under the terms in [`LICENSE`](LICENSE).
