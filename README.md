# SafeKeyboard (Android) — iOS风格高颜值安全键盘

一个在应用内使用的安全键盘库，拥有接近 iOS 的视觉风格与键位布局，支持英文与数字输入，支持数字键盘随机排列，可替代系统输入法以减少三方输入法采集风险。

## 特性
- iOS 风格外观与键位：字母键盘与数字键盘
- 英文大小写切换、删除、空格、换行
- 数字键盘可选随机排列，提高安全性
- 纯 View 方案，不注册系统输入法，无剪贴板/联想
- 简单接入，可直接绑定到 EditText

## 📱 效果预览

<img src="https://github.com/tzf1024/SafeKeyboard/blob/main/Screen_recording_20251112_210939-ezgif.com-video-to-gif-converter.gif" alt="功能截图" width="50%" height="50%">


## 快速开始

1) app/build.gradle.kts
```kotlin
dependencies {
    implementation("io.github.tzf1024:safekeyboard:0.1.0")
}
```

2) 布局中加入键盘视图
```xml
<com.tzf.safekeyboard.IOSKeyboardView
    android:id="@+id/safeKeyboard"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:visibility="gone"/>
```

3) 代码中绑定 `EditText`
```kotlin
val keyboardView = findViewById<IOSKeyboardView>(R.id.safeKeyboard)
val controller = SafeKeyboardController(keyboardView)
controller.setRandomizeNumberPad(true) // 可选：随机数字
controller.attach(findViewById(R.id.input))
```

## API
- `IOSKeyboardView`：键盘 View
  - `setMode(LETTERS|NUMBERS)` 切换键盘类型
  - `setShift(Boolean)` 设置大小写
  - `setRandomizeNumberPad(Boolean)` 设置数字随机
- `SafeKeyboardController`：控制器
  - `attach(EditText)` 绑定输入框（自动禁用系统键盘）
  - `setRandomizeNumberPad(Boolean)` 配置数字键盘随机
  - `show()/hide()` 显示/隐藏键盘

## 注意
- 本库用于应用内安全输入场景，不会注册系统输入法
- Demo 默认启用 `FLAG_SECURE` 禁止截屏

## 许可
Apache-2.0


