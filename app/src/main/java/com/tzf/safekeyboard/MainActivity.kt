package com.tzf.safekeyboard

import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tzf.safekeyboard.IOSKeyboardView
import com.tzf.safekeyboard.SafeKeyboardController

class MainActivity : AppCompatActivity() {
    private lateinit var controller: SafeKeyboardController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Prevent screenshots for demo security
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        val input = findViewById<EditText>(R.id.input)
        val keyboardView = findViewById<IOSKeyboardView>(R.id.safeKeyboard)
        controller = SafeKeyboardController(keyboardView)
        controller.setRandomizeNumberPad(true)
        controller.attach(input)
    }
}