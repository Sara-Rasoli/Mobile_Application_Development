# TDM Insight — Member 1 (UI & Navigation) Deliverable

This covers exactly Member 1's scope from the Group Coding Agreement (Section 4)
plus the shared model classes everyone needs (Section 7), so the project
compiles and runs end-to-end today.

## What's included

**Shared models (Section 7)** — `model/`
- `PatientInfo.kt`
- `TDMWorkflow.kt`
- `TDMInput.kt` — sealed class with `Pre` / `Post` / `PrePost` variants, per
  the group's navigation-design refinement. Has `TODO(Member 2)` markers
  where the real clinical fields go once the lecturer-approved spec is set.
- `TDMResult.kt` — nullable fields, since not every workflow produces every
  output.
- `CalculationStep.kt`

**Member 1 screens** — `ui/screens/`
- `HomeScreen.kt`
- `CreateCaseScreen.kt`
- `PatientInformationScreen.kt` (with light "is this a valid number" UI
  checks — NOT a replacement for Member 2's TDMValidator)
- `WorkflowSelectionScreen.kt`
- `ReviewScreen.kt`

**Navigation** — `ui/navigation/`
- `AppRoutes.kt` — route name constants only (kept separate as agreed, so it
  can exist before every screen is built).
- `AppNavigation.kt` — the actual `NavHost` graph wiring every screen
  together in the agreed order: Home → Create Case → Patient Information →
  Workflow Selection → Dynamic Input → Review → Results →
  Calculation Explanation.
- `CaseViewModel.kt` — a nav-graph-scoped ViewModel that carries
  `PatientInfo` and the selected `TDMWorkflow` across screens, so data
  entered early in the flow is still available on the Review screen.

**Placeholders so the app actually runs today** — `ui/screens/`
- `DynamicInputScreen.kt` — stub, owned by **Member 2**
- `ResultsScreen.kt`, `CalculationExplanationScreen.kt` — stubs, owned by
  **Member 3**

These stubs exist purely so `AppNavigation.kt` compiles and the whole flow
is tappable end-to-end. Each has a `TODO(Member X)` comment. Please replace
the body, not the function signature, unless you flag the change first
(Section 12 — these sit on the shared navigation path).

**App scaffolding**
- `MainActivity.kt` — now just sets the theme and calls `AppNavigation()`
- `ui/theme/Theme.kt` — minimal Material 3 theme
- `AndroidManifest.xml`
- `build.gradle.kts` (root + `app/`), `settings.gradle.kts`,
  `gradle/libs.versions.toml` — includes the Navigation Compose and
  ViewModel-Compose dependencies that Section 2 of the notes flagged as
  needed before writing navigation code.

## How to integrate into the shared repo

1. Create/checkout your feature branch: `feature/ui-navigation`
2. Copy this project's contents into the shared `TDM-Insight` repo (or, if
   the repo is empty, use this as the initial commit for the Android
   project skeleton — Integration Order step 1–4 in Section 13).
3. Open in Android Studio, let Gradle sync, run on an emulator — you should
   be able to tap through Home → Create Case → Patient Info → Workflow
   Selection → (placeholder Dynamic Input) → Review → (placeholder Results)
   → (placeholder Explanation).
4. Commit with a clear message, e.g. `Add UI, navigation, and shared models`,
   push, and open a Pull Request per Section 10.

## Handoff notes for teammates

- **Member 2**: your real estate is `ui/components/` (`PreInputForm.kt`,
  `PostInputForm.kt`, `PrePostInputForm.kt`) and `validation/TDMValidator.kt`,
  plus replacing the body of `DynamicInputScreen.kt`. `TDMInput.kt` has
  `TODO`s waiting for the lecturer-approved fields.
- **Member 3**: your real estate is `calculation/` and replacing the bodies
  of `ResultsScreen.kt` / `CalculationExplanationScreen.kt`. `TDMResult.kt`
  is ready for you to populate.
- Nobody should need to touch `AppNavigation.kt` or `AppRoutes.kt` to do
  their own work — just flag it in the group chat if you do (Section 12).
