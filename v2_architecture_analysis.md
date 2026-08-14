# Imperium V2 Architecture & Gaming Algorithm Analysis

## Current Architecture Review
The current application employs a mixed approach. While it utilizes modern Android components like Jetpack Compose, Hilt, and Room, the architecture leans towards a standard MVVM pattern but suffers from significant coupling and logic misplacement.

### 1. `QuizScreen.kt` (UI Layer)
- **Spaghetti Code / SOLID Violations:** The UI file contains hardcoded lists of domain data (`ancientQuestions`, `medievalQuestions`, etc.) that are completely detached from the database logic handled by the ViewModel. This violates the **Single Responsibility Principle (SRP)**. The UI should only concern itself with rendering, not acting as a data source.
- **Tightly Coupled Logic:** The UI manages timer callbacks, handles complex state transitions, and manages color logic directly inline. The screen is monolithic and hard to unit test.
- **Factory Instantiation in Compose:** Instantiating the `QuizViewModel` using a manual factory inside the composable instead of injecting it fully via Hilt reduces the benefits of Dependency Injection.

### 2. `QuizViewModel.kt` (Presentation/Domain Layer)
- **Bloated Responsibility (God Object):** The ViewModel manages quiz state, loads questions, handles timers, calculates scores, and directly communicates with the `QuizRepository` to unlock levels. This violates SRP.
- **Lack of Domain Layer (Use Cases):** There is no clear domain layer. Business rules, like calculating stars based on percentages or determining if a level should be unlocked, are mixed into the ViewModel and Repository.
- **Timer Handling:** The timer logic is split between the UI and ViewModel in a clunky way, making it fragile and susceptible to lifecycle issues.

### 3. `QuizRepository.kt` (Data Layer)
- **Business Logic in Repository:** The `unlockLevel`, `updateLevelProgress`, and `calculateStars` functions contain significant business logic. A repository should only be responsible for abstracting data sources (fetching/saving data), not making decisions about gameplay progression (e.g., "if level 1 has stars > 0, unlock level 2"). This violates the **Single Responsibility Principle**.
- **Hardcoded Rules:** Rules like randomizing questions or taking exactly 4 questions are hardcoded across the data layer instead of being configurable domain policies.

## Existing Bugs & Risks in Gaming Logic
1. **Timer Sync Issue:** The timer is heavily dependent on UI ticks via `rememberQuizTimer`. If the app goes to the background, the timer behavior becomes unpredictable.
2. **State Leakage:** `selectAnswer` delays logic using coroutines directly in the UI layer (`delay(1500)`). If the user navigates away or mashes buttons during this delay, it can lead to state inconsistency or crashes.
3. **Data Source Conflict:** The app seems to have both a Room Database (managed by `QuizRepository`) and hardcoded Kotlin lists in `QuizScreen`. It's unclear which is the absolute source of truth, leading to potential data divergence.

---

## Proposed Clean Architecture (OOP & SOLID) for V2

To elevate the app to an enterprise level, we will implement **Clean Architecture**.

### 1. Domain Layer (The Core)
This layer will contain pure Kotlin/Java business logic, free of Android dependencies.
- **Entities:** `Quiz`, `Question`, `Answer`, `UserProgress`.
- **Use Cases (Interactors):**
  - `LoadQuizUseCase`: Fetches questions and shuffles them according to game rules.
  - `SubmitAnswerUseCase`: Validates the answer and updates the current score.
  - `CalculateQuizResultUseCase`: Evaluates the final score, calculates stars, and determines level unlocking logic.
  - `ManageTimerUseCase`: A robust, lifecycle-aware timer engine decoupled from the UI.

### 2. Data Layer (Repository Implementation)
- **Interfaces:** Defined in the Domain layer (e.g., `IQuizRepository`, `IProgressRepository`).
- **Implementations:** Classes in the Data layer that implement these interfaces, dealing solely with Room DAOs and mapping Data Entities to Domain Entities. **No business logic.**

### 3. Presentation Layer (MVVM + MVI)
- **ViewModel:** Refactored to act as a bridge. It will observe flows from Use Cases and emit a single, immutable UI State (e.g., `QuizUiState`).
- **UI (Jetpack Compose):**
  - Complete removal of hardcoded data.
  - Broken down into smaller, reusable components (`QuestionCard`, `TimerBar`, `AnswerList`).
  - Hilt will inject the ViewModel via `@HiltViewModel`, removing the manual factory in the Composable.

### Summary
This refactoring will strictly separate concerns, making the gaming algorithm fully unit-testable without requiring an Android emulator, ensuring the app is scalable, maintainable, and stable.