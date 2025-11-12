package com.tzf.safekeyboard

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class IOSKeyboardView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

	enum class Mode {
		LETTERS, NUMBERS
	}

	sealed class KeyAction {
		data class InputChar(val char: Char) : KeyAction()
		data object Delete : KeyAction()
		data object Shift : KeyAction()
		data object Space : KeyAction()
		data object Enter : KeyAction()
		data class SwitchMode(val mode: Mode) : KeyAction()
		data object Hide : KeyAction()
	}

	var onKeyPressed: ((KeyAction) -> Unit)? = null

	private val keyboardContainer: LinearLayout = LinearLayout(context)
	private var currentMode: Mode = Mode.LETTERS
	private var isShiftOn: Boolean = false

	private var shouldRandomizeNumbers: Boolean = false

	init {
		setBackgroundResource(R.drawable.sk_keyboard_bg)
		val padding = resources.getDimensionPixelSize(R.dimen.sk_keyboard_padding)
		setPadding(padding, padding, padding, padding)
		keyboardContainer.orientation = LinearLayout.VERTICAL
		addView(
			keyboardContainer,
			LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
		)
		render()
	}

	fun setMode(mode: Mode) {
		if (mode == currentMode) return
		currentMode = mode
		render()
	}

	fun getMode(): Mode = currentMode

	fun setShift(on: Boolean) {
		isShiftOn = on
		if (currentMode == Mode.LETTERS) {
			render()
		}
	}

	fun isShiftOn(): Boolean = isShiftOn

	fun setRandomizeNumberPad(randomize: Boolean) {
		shouldRandomizeNumbers = randomize
		if (currentMode == Mode.NUMBERS) {
			render()
		}
	}

	private fun render() {
		keyboardContainer.removeAllViews()
		when (currentMode) {
			Mode.LETTERS -> renderLetters()
			Mode.NUMBERS -> renderNumbers()
		}
	}

	private fun createRow(): LinearLayout {
		val row = LinearLayout(context)
		row.orientation = LinearLayout.HORIZONTAL
		row.gravity = Gravity.CENTER
		return row
	}

	private fun button(text: String, isSecondary: Boolean = false, weight: Float = 0f): AppCompatButton {
		val btn = AppCompatButton(context)
		btn.text = text
		btn.setTextColor(ContextCompat.getColor(context, if (isSecondary) R.color.sk_key_secondary_text else R.color.sk_key_text))
		btn.textSize = resources.getDimension(R.dimen.sk_key_text_size) / resources.displayMetrics.scaledDensity
		btn.minHeight = resources.getDimensionPixelSize(R.dimen.sk_key_height)
		btn.minWidth = 0
		btn.isAllCaps = false
		btn.stateListAnimator = null
		btn.background = ContextCompat.getDrawable(context, if (isSecondary) R.drawable.sk_key_secondary_bg else R.drawable.sk_key_bg)
		val lp = LinearLayout.LayoutParams(0, resources.getDimensionPixelSize(R.dimen.sk_key_height), if (weight > 0f) weight else 1f)
		val m = resources.getDimensionPixelSize(R.dimen.sk_key_margin)
		lp.setMargins(m, m, m, m)
		btn.layoutParams = lp
		return btn
	}

	private fun spacer(weight: Float): View {
		val v = View(context)
		v.layoutParams = LinearLayout.LayoutParams(0, 0, weight)
		v.isVisible = false
		return v
	}

	private fun renderLetters() {
		val rows = listOf(
			"q w e r t y u i o p",
			"a s d f g h j k l",
			"z x c v b n m"
		)
		for (i in rows.indices) {
			val rowLayout = createRow()
			if (i == 1) {
				rowLayout.addView(spacer(0.5f))
			}
			if (i == 2) {
				val shiftBtn = button(if (isShiftOn) "⇧" else "⇧", isSecondary = true, weight = 1.3f)
				shiftBtn.setOnClickListener { onKeyPressed?.invoke(KeyAction.Shift) }
				rowLayout.addView(shiftBtn)
			}
			val parts = rows[i].split(" ")
			for (p in parts) {
				val label = if (isShiftOn) p.uppercase() else p
				val keyBtn = button(label, isSecondary = false)
				keyBtn.setOnClickListener {
					val ch = label[0]
					onKeyPressed?.invoke(KeyAction.InputChar(ch))
				}
				rowLayout.addView(keyBtn)
			}
			if (i == 2) {
				val delBtn = button("⌫", isSecondary = true, weight = 1.3f)
				delBtn.setOnClickListener { onKeyPressed?.invoke(KeyAction.Delete) }
				rowLayout.addView(delBtn)
			}
			if (i == 1) {
				rowLayout.addView(spacer(0.5f))
			}
			keyboardContainer.addView(rowLayout, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
		}
		val bottom = createRow()
		val numbersBtn = button("123", isSecondary = true, weight = 1.8f)
		numbersBtn.setOnClickListener { onKeyPressed?.invoke(KeyAction.SwitchMode(Mode.NUMBERS)) }
		val spaceBtn = button("space", isSecondary = false, weight = 5f)
		spaceBtn.setOnClickListener { onKeyPressed?.invoke(KeyAction.Space) }
		val enterBtn = button("return", isSecondary = true, weight = 1.8f)
		enterBtn.setOnClickListener { onKeyPressed?.invoke(KeyAction.Enter) }
		bottom.addView(numbersBtn)
		bottom.addView(spaceBtn)
		bottom.addView(enterBtn)
		keyboardContainer.addView(bottom, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
	}

	private fun renderNumbers() {
		val digitsOneToNine = ('1'..'9').toMutableList()
		if (shouldRandomizeNumbers) {
			digitsOneToNine.shuffle()
		}
		var index = 0
		repeat(3) {
			val row = createRow()
			repeat(3) {
				val c = digitsOneToNine[index]
				index++
				val b = button(c.toString())
				b.setOnClickListener { onKeyPressed?.invoke(KeyAction.InputChar(c)) }
				row.addView(b)
			}
			keyboardContainer.addView(row)
		}
		val bottom = createRow()
		val lettersBtn = button("ABC", isSecondary = true)
		lettersBtn.setOnClickListener { onKeyPressed?.invoke(KeyAction.SwitchMode(Mode.LETTERS)) }
		val zeroBtn = button("0")
		zeroBtn.setOnClickListener { onKeyPressed?.invoke(KeyAction.InputChar('0')) }
		val delBtn = button("⌫", isSecondary = true)
		delBtn.setOnClickListener { onKeyPressed?.invoke(KeyAction.Delete) }
		bottom.addView(lettersBtn)
		bottom.addView(zeroBtn)
		bottom.addView(delBtn)
		keyboardContainer.addView(bottom)
	}
}


