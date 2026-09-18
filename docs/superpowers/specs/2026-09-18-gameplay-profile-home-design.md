# Gameplay, Profile, and Home Design

## Scope

All work remains on `feature/stitch-ui-redesign-test`. The change activates two quiz lifelines, removes Oraculum completely, makes long questions responsive, separates Profile/Badges/Settings, and corrects Home sizing and resume behavior. Existing scoring, question correctness, unlocking, and attempt persistence remain unchanged.

## Quiz lifelines

- Mora adds 15 seconds to an active unanswered question, capped at 45 seconds.
- 50/50 hides exactly two incorrect answers using a deterministic selection derived from the session seed, question id, and question position.
- Each lifeline can be used once per quiz session and is disabled after use, after answer reveal, or after timeout.
- Lifeline state is part of `QuizSession` and is persisted/restored by `QuizViewModel`.
- Oraculum UI, copy, and any related code are removed.

## Responsive quiz layout

Timer/progress and the bottom feedback/action area remain fixed. The center region remains scrollable for accessibility. A long-question presentation removes the decorative historical image and compresses metadata/spacing so four-line questions and four ordinary answer options fit on a standard phone. Very small screens, large fonts, or unusually long answers retain scrolling and expose a clear lower-edge affordance rather than truncating text.

## Home

The three cards use the available vertical space more evenly, and Expeditions receives a minimum height matching its visual importance. Short screens remain scrollable. Continue Expedition is driven by the most recently played category, using persisted category progress timestamps; categories with no completed play history do not outrank played categories. Ancient Civilizations is the deterministic fallback for a new user.

## Profile information architecture

- Profile becomes a progress dashboard: identity/rank header, core statistics, category mastery, current expedition, and navigation cards for Badges and Settings.
- Badges moves to `BadgesScreen`, retaining real `BadgeViewModel` data and earned-state behavior.
- Sound effects and background music move to `SettingsScreen`, backed by the existing `SoundManager` persistence.
- Profile, Badges, and Settings use native Compose and existing Imperium visual tokens. Badges and Settings are secondary destinations with explicit back navigation.

## Validation

Pure domain/presentation decisions receive unit tests. Build and focused tests must pass. Emulator review covers initial quiz, both lifelines, long question layout, Home, Profile, Badges, and Settings. No fake user or gameplay data is introduced.
