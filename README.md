# Restaurant Discovery — Android
Native Kotlin / Jetpack Compose implementation of the restaurant discovery brief. Fictional Johannesburg restaurants, with a local JSON source explicitly permitted by the assignment. No account or API key required.

## Run
Prerequisites: JDK 17, Android SDK 35, and an Android device/emulator API 26+. The committed Gradle wrapper pins Gradle 9.8.1. Dependency downloads require internet; runtime discovery does not.

From this directory run:

```sh
./gradlew assembleDebug testDebugUnitTest lintDebug
./gradlew installDebug
adb shell am start -n co.za.discovery/.MainActivity
```

Alternatively open the root directory in Android Studio, sync and run `app`. Configure SDK location through Android Studio or local.properties (`sdk.dir=...`). Keep `gradlew`, `gradlew.bat`, and `gradle/wrapper/*` committed before submitting your repository.

## Included
- 240 deterministic, fictional restaurants; name search and Open now.
- Browse and details; back navigation; cents-based ZAR fees; safe unknown-field presentation.
- Favourites persisted across force-stop and relaunch, shared between both screens.
- Loading, no results, error with retry, and nonblocking cached-list refresh states.
- Local JSON adapter, atomic last-successful cache and lifecycle-aware state collection.
- Unit-test sources for search/filter behaviour and defensive JSON decoding.

## Demonstrate failure and offline behaviour
1. Launch normally once; scroll, save a restaurant, force-stop and relaunch.
2. Enable airplane mode. Force-stop and launch: all restaurants remain available from local cache/assets.
3. Build/install the debug variant with `./gradlew installDebug -PsimulateFailure=true` WITHOUT uninstalling the app. Refresh or relaunch: the cached list remains visible alongside the retry banner.
4. To test initial failure, clear app data while running the failure variant. Launch: initial error + retry, no stale data.
5. Reinstall normal debug (`-PsimulateFailure=false`) without uninstalling; retry should recover.

Failure simulation applies only to debug builds. This demonstrates source failure; the default source is an asset, so airplane mode alone does not produce a network error. No live REST endpoint is claimed.
