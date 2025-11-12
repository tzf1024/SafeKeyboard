# SafeKeyboard (Android) — iOS‑style, high‑polish in‑app secure keyboard

An in‑app secure keyboard library with an iOS‑like look and key layout. It supports English letters and a numeric keypad, optional randomized number arrangement, and can replace the system IME to reduce the risk of third‑party input methods collecting data.

## Features
- iOS‑style appearance and key layout: letter keyboard and number keypad
- English casing toggle, delete, space, and return
- Optional randomized number keypad to enhance security
- Pure View solution: does not register as a system IME; no clipboard use or suggestions
- Simple integration; can be bound directly to `EditText`

## Preview
After integration, the keyboard is fixed at the bottom of the page. It automatically shows when an `EditText` gains focus and hides when it loses focus.

## Quick Start

1) app/build.gradle.kts
```kotlin
dependencies {
    implementation(project(":safekeyboard"))
}
```

2) Add the keyboard view in your layout
```xml
<com.tzf.safekeyboard.IOSKeyboardView
    android:id="@+id/safeKeyboard"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:visibility="gone"/>
```

3) Bind to an `EditText` in code
```kotlin
val keyboardView = findViewById<IOSKeyboardView>(R.id.safeKeyboard)
val controller = SafeKeyboardController(keyboardView)
controller.setRandomizeNumberPad(true) // Optional: randomize numbers
controller.attach(findViewById(R.id.input))
```

## API
- `IOSKeyboardView`: the keyboard View
  - `setMode(LETTERS|NUMBERS)`: switch keyboard type
  - `setShift(Boolean)`: set letter casing
  - `setRandomizeNumberPad(Boolean)`: enable randomized number keypad
- `SafeKeyboardController`: controller
  - `attach(EditText)`: bind an input field (disables the system keyboard automatically)
  - `setRandomizeNumberPad(Boolean)`: configure randomized number keypad
  - `show()/hide()`: show/hide the keyboard

## Notes
- This library is intended for in‑app secure input scenarios and will not register as a system IME
- The demo enables `FLAG_SECURE` by default to block screenshots

## License
Apache-2.0


