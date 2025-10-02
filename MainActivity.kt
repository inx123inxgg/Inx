package com.example.autotext

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import android.os.Handler
import android.os.Looper
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var button: Button
    private lateinit var delayInput: EditText
    private lateinit var customTextsInput: EditText
    private var running = false
    private var index = 0
    private var delayMillis: Long = 2000 // Default 2 sec
    private val handler = Handler(Looper.getMainLooper())

    private var texts: List<String> = listOf("Hello", "Welcome", "Auto Text Running", "Android 13 Project")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        button = findViewById(R.id.button)
        delayInput = findViewById(R.id.delayInput)
        customTextsInput = findViewById(R.id.customTextsInput)

        button.setOnClickListener {
            running = !running
            if (running) {
                button.text = "Stop"
                index = 0 // Reset index when starting
                cycleText()
            } else {
                button.text = "Start"
                handler.removeCallbacksAndMessages(null)
            }
        }
    }

    private fun cycleText() {
        if (running) {
            // 🔹 Get custom texts from user
            val userInput = customTextsInput.text.toString()
            texts = if (userInput.isNotEmpty()) {
                userInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            } else {
                listOf("Hello", "Welcome", "Auto Text Running", "Android 13 Project")
            }

            val currentText = texts[index % texts.size]
            textView.text = currentText

            // 🔹 Auto Copy to Clipboard
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("autoText", currentText)
            clipboard.setPrimaryClip(clip)

            // 🔹 Get delay from user input, default = 2 sec
            val input = delayInput.text.toString()
            delayMillis = if (input.isNotEmpty()) input.toLong() * 1000 else 2000

            index++
            handler.postDelayed({ cycleText() }, delayMillis)
        }
    }
}
