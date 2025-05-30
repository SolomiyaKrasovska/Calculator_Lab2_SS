package com.kotlin.calculator_lab2_s

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kotlin.calculator_lab2_s.R
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private lateinit var textViewDisplay: TextView
    private var currentInput = ""
    private var firstNumber: Double? = null
    private var operation: Char? = null
    private var memory: Double = 0.0
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewDisplay = findViewById(R.id.textViewDisplay)

        // Ініціалізація кнопок цифр
        val numberButtons = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9
        )
        numberButtons.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                appendNumber((it as Button).text.toString())
            }
        }

        // Кнопка крапки
        findViewById<Button>(R.id.buttonDot).setOnClickListener {
            if (!currentInput.contains(".")) {
                appendNumber(".")
            }
        }

        // Кнопка очищення
        findViewById<Button>(R.id.buttonClear).setOnClickListener {
            clear()
        }

        // Кнопка видалення останнього символу
        findViewById<Button>(R.id.buttonBackspace).setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = currentInput.dropLast(1)
                updateDisplay()
            }
        }

        // Кнопки операцій
        findViewById<Button>(R.id.buttonAdd).setOnClickListener { setOperation('+') }
        findViewById<Button>(R.id.buttonSubtract).setOnClickListener { setOperation('-') }
        findViewById<Button>(R.id.buttonMultiply).setOnClickListener { setOperation('*') }
        findViewById<Button>(R.id.buttonDivide).setOnClickListener { setOperation('/') }
        findViewById<Button>(R.id.buttonPower).setOnClickListener { setOperation('^') }

        // Кнопка результату
        findViewById<Button>(R.id.buttonEquals).setOnClickListener {
            calculateResult()
        }

        // Кнопки пам’яті
        findViewById<Button>(R.id.buttonMemoryStore).setOnClickListener {
            if (currentInput.isNotEmpty()) {
                memory = currentInput.toDouble()
                Toast.makeText(this, "Збережено в пам’ять: $memory", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<Button>(R.id.buttonMemoryRecall).setOnClickListener {
            currentInput = memory.toString()
            updateDisplay()
        }
        findViewById<Button>(R.id.buttonMemoryClear).setOnClickListener {
            memory = 0.0
            Toast.makeText(this, "Пам’ять очищена", Toast.LENGTH_SHORT).show()
        }
    }

    private fun appendNumber(number: String) {
        if (isNewOperation) {
            currentInput = ""
            isNewOperation = false
        }
        currentInput += number
        updateDisplay()
    }

    private fun setOperation(op: Char) {
        if (currentInput.isEmpty()) {
            Toast.makeText(this, "Введи число", Toast.LENGTH_SHORT).show()
            return
        }
        firstNumber = currentInput.toDouble()
        operation = op
        isNewOperation = true
    }

    private fun calculateResult() {
        if (firstNumber == null || operation == null || currentInput.isEmpty()) {
            Toast.makeText(this, "Введи всі дані", Toast.LENGTH_SHORT).show()
            return
        }

        val secondNumber = currentInput.toDouble()
        val result = when (operation) {
            '+' -> firstNumber!! + secondNumber
            '-' -> firstNumber!! - secondNumber
            '*' -> firstNumber!! * secondNumber
            '/' -> {
                if (secondNumber == 0.0) {
                    Toast.makeText(this, "Ділення на нуль неможливе", Toast.LENGTH_SHORT).show()
                    return
                }
                firstNumber!! / secondNumber
            }
            '^' -> firstNumber!!.pow(secondNumber)
            else -> 0.0
        }

        currentInput = result.toString()
        firstNumber = null
        operation = null
        isNewOperation = true
        updateDisplay()
    }

    private fun clear() {
        currentInput = ""
        firstNumber = null
        operation = null
        isNewOperation = true
        updateDisplay()
    }

    private fun updateDisplay() {
        textViewDisplay.text = if (currentInput.isEmpty()) "0" else currentInput
    }
}