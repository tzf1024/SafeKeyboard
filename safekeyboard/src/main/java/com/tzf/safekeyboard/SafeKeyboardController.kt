package com.tzf.safekeyboard

import android.text.Editable
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible

class SafeKeyboardController(
	private val keyboardView: IOSKeyboardView
) {
	private var attachedEditText: EditText? = null
	private var randomizeNumbers: Boolean = false

	init {
		keyboardView.onKeyPressed = { action ->
			when (action) {
				is IOSKeyboardView.KeyAction.InputChar -> insertChar(action.char)
				IOSKeyboardView.KeyAction.Delete -> deleteChar()
				IOSKeyboardView.KeyAction.Shift -> toggleShift()
				IOSKeyboardView.KeyAction.Space -> insertChar(' ')
				IOSKeyboardView.KeyAction.Enter -> handleEnter()
				is IOSKeyboardView.KeyAction.SwitchMode -> switchMode(action.mode)
				IOSKeyboardView.KeyAction.Hide -> hide()
			}
		}
	}

	fun attach(editText: EditText) {
		attachedEditText = editText
		// Disable system keyboard
		editText.showSoftInputOnFocus = false
		editText.inputType = editText.inputType and InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS.inv()

		editText.setOnFocusChangeListener { v, hasFocus ->
			if (hasFocus) {
				show()
			} else {
				hide()
			}
		}
		editText.setOnClickListener {
			show()
		}
	}

	fun setRandomizeNumberPad(randomize: Boolean) {
		randomizeNumbers = randomize
		keyboardView.setRandomizeNumberPad(randomizeNumbers)
	}

	fun show() {
		keyboardView.isVisible = true
		// Also hide system keyboard if any
		val et = attachedEditText ?: return
		val imm = et.context.getSystemService(InputMethodManager::class.java)
		imm?.hideSoftInputFromWindow(et.windowToken, 0)
	}

	fun hide() {
		keyboardView.isVisible = false
	}

	private fun switchMode(mode: IOSKeyboardView.Mode) {
		keyboardView.setMode(mode)
		// keep shift state for letter mode
	}

	private fun toggleShift() {
		keyboardView.setShift(!keyboardView.isShiftOn())
	}

	private fun handleEnter() {
		val et = attachedEditText ?: return
		val isMultiLine = et.inputType and InputType.TYPE_TEXT_FLAG_MULTI_LINE != 0
		if (isMultiLine) {
			insertChar('\n')
		} else {
			et.clearFocus()
			hide()
		}
	}

	private fun insertChar(c: Char) {
		val et = attachedEditText ?: return
		val editable: Editable = et.text
		val start = et.selectionStart.coerceAtLeast(0)
		val end = et.selectionEnd.coerceAtLeast(0)
		val toInsert = if (keyboardView.isShiftOn() && keyboardView.getMode() == IOSKeyboardView.Mode.LETTERS) {
			c.uppercaseChar()
		} else {
			c
		}
		editable.replace(start.coerceAtMost(end), end.coerceAtLeast(start), toInsert.toString())
	}

	private fun deleteChar() {
		val et = attachedEditText ?: return
		val editable: Editable = et.text
		val start = et.selectionStart
		val end = et.selectionEnd
		if (start != end) {
			editable.delete(start, end)
			return
		}
		val idx = (start - 1).coerceAtLeast(0)
		if (idx < editable.length && start > 0) {
			editable.delete(idx, start)
		}
	}
}


