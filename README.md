# line-connect

A Flow Free–style puzzle game for Android, built with Kotlin and Jetpack Compose.
Drag to connect each pair of same-colored dots; pipes can't cross. Based on a classic from my childhood

## Features

- **Three board sizes** — 5×5, 7×7 and 10×10, each with a 15-level pack.
- **Random boards** — a built-in generator makes fresh puzzles for every size.

## Building

Open the project in Android Studio and press **Run**, or from the command line:

```sh
./gradlew assembleDebug        # build the APK
./gradlew testDebugUnitTest    # run the unit tests
```

## Project layout

`app/src/main/java/com/example/lineconnect/flow/`

| Package | Contents |
|---|---|
| `model/` | Board sizes, levels and the drag/pipe rules (`FlowGame`) |
| `levels/` | Built-in level packs for each size |
| `generator/` | Random board generator and the connection counter it uses |
| `data/` | Saved progress |
| `ui/` | Compose screens and the board canvas |
