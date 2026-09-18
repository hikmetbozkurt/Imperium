# Gameplay, Profile, and Home Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Activate two reliable quiz lifelines, support long questions, reorganize Profile/Badges/Settings, and correct Home sizing and resume behavior.

**Architecture:** Gameplay rules stay in immutable `QuizSession`; ViewModels expose events and persistence; Compose components only render state and forward actions. Home resume data comes through `CategoryProgress`, while Profile subpages reuse existing progress, badge, and sound sources.

**Tech Stack:** Kotlin, Jetpack Compose, StateFlow, Hilt, Room, SavedStateHandle, JUnit.

**Spec:** `docs/superpowers/specs/2026-09-18-gameplay-profile-home-design.md`

## Global Constraints

- Work only on `feature/stitch-ui-redesign-test`; do not merge into `main`.
- Use one agent and no fake gameplay/profile data.
- Preserve scoring, correctness, unlocking, navigation contracts, and attempt persistence unless explicitly described in the spec.
- Do not commit unrelated `.idea` changes.

---

### Task 1: Quiz lifeline domain and persistence

**Files:**
- Modify: `app/src/main/java/com/hikmet/imperium/domain/game/QuizSession.kt`
- Modify: `app/src/main/java/com/hikmet/imperium/feature/quiz/QuizViewModel.kt`
- Modify: `app/src/test/java/com/hikmet/imperium/domain/game/QuizSessionTest.kt`
- Modify: `app/src/test/java/com/hikmet/imperium/feature/quiz/QuizViewModelTest.kt`

**Interfaces:**
- Produces: `QuizSession.addTime()`, `QuizSession.useFiftyFifty()`, `moraUsed`, `fiftyFiftyUsed`, and `hiddenOptionIndices`.

- [ ] Add failing tests proving +15 seconds caps at 45 seconds, each helper is single-use, 50/50 hides two incorrect choices deterministically, and revealed questions reject helpers.
- [ ] Run the focused tests and confirm the new API is missing.
- [ ] Implement immutable domain transitions and ViewModel entry points.
- [ ] Persist and restore helper state through `SavedStateHandle`.
- [ ] Run focused tests and commit the gameplay slice.

### Task 2: Lifeline UI and long-question layout

**Files:**
- Modify: `app/src/main/java/com/hikmet/imperium/feature/quiz/QuizScreen.kt`
- Modify: `app/src/main/java/com/hikmet/imperium/feature/quiz/ImperiumQuizDesign.kt`
- Modify: `app/src/main/res/values/strings.xml`
- Modify: `app/src/test/java/com/hikmet/imperium/feature/quiz/QuizPresentationTest.kt`

**Interfaces:**
- Consumes: Task 1 lifeline state/actions.
- Produces: two evenly spaced actionable lifeline controls and `questionPresentation(...)` density selection.

- [ ] Add failing presentation tests for two-button alignment/state and long-question compact presentation.
- [ ] Remove Oraculum copy and component usage.
- [ ] Bind Mora and 50/50 to real ViewModel actions and hide eliminated answer tiles without changing option indexes.
- [ ] Collapse the decorative image and tighten spacing for measured long questions; retain scroll affordance for constrained screens.
- [ ] Run quiz tests/build and commit the UI slice.

### Task 3: Real Home resume behavior and balanced cards

**Files:**
- Modify: `app/src/main/java/com/hikmet/imperium/domain/repository/GameProgressRepository.kt`
- Modify: `app/src/main/java/com/hikmet/imperium/data/repository/RoomGameProgressRepository.kt`
- Modify: `app/src/main/java/com/hikmet/imperium/ui/home/HomePresentation.kt`
- Modify: `app/src/main/java/com/hikmet/imperium/ui/home/HomeScreen.kt`
- Modify: `app/src/test/java/com/hikmet/imperium/ui/home/HomePresentationTest.kt`

**Interfaces:**
- Produces: `CategoryProgress.lastPlayedTimestamp` and deterministic `selectCurrentExpedition` behavior.

- [ ] Add failing tests selecting the latest actually played category and Ancient as the new-user fallback.
- [ ] Expose persisted category timestamps through the repository mapping.
- [ ] Balance card minimum heights/available-space distribution while retaining short-screen scrolling.
- [ ] Run Home/repository tests and commit the Home slice.

### Task 4: Profile, Badges, and Settings navigation

**Files:**
- Modify: `app/src/main/java/com/hikmet/imperium/ui/profile/ProfileScreen.kt`
- Create: `app/src/main/java/com/hikmet/imperium/ui/profile/BadgesScreen.kt`
- Create: `app/src/main/java/com/hikmet/imperium/ui/settings/SettingsScreen.kt`
- Modify: `app/src/main/java/com/hikmet/imperium/ui/navigation/NavGraph.kt`
- Modify: `app/src/main/res/values/strings.xml`
- Test: `app/src/test/java/com/hikmet/imperium/ui/profile/ProfilePresentationTest.kt`

**Interfaces:**
- Produces: `BADGES_ROUTE`, `SETTINGS_ROUTE`, progress-derived profile sections, and dedicated secondary screens.

- [ ] Add failing tests for rank/mastery presentation helpers using real progress values.
- [ ] Redesign Profile as progress summary and add Badges/Settings navigation cards.
- [ ] Move badge rendering and audio toggles to their dedicated screens.
- [ ] Add routes/back behavior and remove obsolete Profile-owned preference UI.
- [ ] Run profile/navigation tests and commit the information-architecture slice.

### Task 5: Regression and delivery

**Files:**
- Verify all modified production/test files and plan/spec documents.

- [ ] Run `gradlew.bat testDebugUnitTest assembleDebug --offline`.
- [ ] Run `git diff --check` and verify no data/domain behavior outside the approved scope changed.
- [ ] Review key screens/states on the emulator when available.
- [ ] Commit any final corrections, push `feature/stitch-ui-redesign-test` to `origin`, and leave `main` untouched.
