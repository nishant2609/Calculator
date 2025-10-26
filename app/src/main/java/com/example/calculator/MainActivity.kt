package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var displayExpression: TextView
    private lateinit var displayResult: TextView
    private lateinit var basicLayout: ConstraintLayout
    private lateinit var scientificLayout: ScrollView
    private lateinit var btnToggleScientific: ImageButton

    private var isScientificMode = false
    private var currentExpression = ""
    private var lastResult = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupToggleButton()
        setupBasicButtons()
        setupScientificButtons()
    }

    private fun initializeViews() {
        displayExpression = findViewById(R.id.displayExpression)
        displayResult = findViewById(R.id.displayResult)
        basicLayout = findViewById(R.id.basicLayout)
        scientificLayout = findViewById(R.id.scientificLayout)
        btnToggleScientific = findViewById(R.id.btnToggleScientific)

        displayExpression.text = "0"
        displayResult.text = ""
    }

    private fun setupToggleButton() {
        btnToggleScientific.setOnClickListener {
            isScientificMode = !isScientificMode
            if (isScientificMode) {
                scientificLayout.visibility = View.VISIBLE
                basicLayout.visibility = View.GONE
            } else {
                scientificLayout.visibility = View.GONE
                basicLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun setupBasicButtons() {
        val numberButtons = mapOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2",
            R.id.btn3 to "3", R.id.btn4 to "4", R.id.btn5 to "5",
            R.id.btn6 to "6", R.id.btn7 to "7", R.id.btn8 to "8",
            R.id.btn9 to "9", R.id.btnDot to "."
        )

        numberButtons.forEach { (id, value) ->
            findViewById<Button>(id)?.setOnClickListener {
                appendToExpression(value)
            }
        }

        findViewById<Button>(R.id.btnAdd)?.setOnClickListener { appendToExpression("+") }
        findViewById<Button>(R.id.btnSubtract)?.setOnClickListener { appendToExpression("-") }
        findViewById<Button>(R.id.btnMultiply)?.setOnClickListener { appendToExpression("×") }
        findViewById<Button>(R.id.btnDivide)?.setOnClickListener { appendToExpression("÷") }

        findViewById<Button>(R.id.btnAC)?.setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnDelete)?.setOnClickListener { deleteLastChar() }
        findViewById<Button>(R.id.btnPercent)?.setOnClickListener { calculatePercent() }
        findViewById<Button>(R.id.btnEquals)?.setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.btnPlusMinus)?.setOnClickListener { toggleSign() }
    }

    private fun setupScientificButtons() {
        // Trigonometric functions
        findViewById<Button>(R.id.btnSin)?.setOnClickListener { appendFunction("sin(") }
        findViewById<Button>(R.id.btnCos)?.setOnClickListener { appendFunction("cos(") }
        findViewById<Button>(R.id.btnTan)?.setOnClickListener { appendFunction("tan(") }
        findViewById<Button>(R.id.btnSinh)?.setOnClickListener { appendFunction("sinh(") }
        findViewById<Button>(R.id.btnCosh)?.setOnClickListener { appendFunction("cosh(") }
        findViewById<Button>(R.id.btnTanh)?.setOnClickListener { appendFunction("tanh(") }

        // Logarithmic
        findViewById<Button>(R.id.btnLn)?.setOnClickListener { appendFunction("ln(") }
        findViewById<Button>(R.id.btnLog)?.setOnClickListener { appendFunction("log(") }

        // Power
        findViewById<Button>(R.id.btnPower2)?.setOnClickListener { appendToExpression("^2") }
        findViewById<Button>(R.id.btnPower3)?.setOnClickListener { appendToExpression("^3") }
        findViewById<Button>(R.id.btnPowerX)?.setOnClickListener { appendToExpression("^") }
        findViewById<Button>(R.id.btnPowerE)?.setOnClickListener { appendToExpression("e^") }
        findViewById<Button>(R.id.btnPower10)?.setOnClickListener { appendToExpression("10^") }

        // Roots & inverse
        findViewById<Button>(R.id.btnSqrt)?.setOnClickListener { appendFunction("sqrt(") }
        findViewById<Button>(R.id.btnCbrt)?.setOnClickListener { appendFunction("cbrt(") }
        findViewById<Button>(R.id.btnRootX)?.setOnClickListener { appendToExpression("^(1/") }
        findViewById<Button>(R.id.btnInverse)?.setOnClickListener { calculateInverse() }

        // Constants & special
        findViewById<Button>(R.id.btnE)?.setOnClickListener { appendToExpression("e") }
        findViewById<Button>(R.id.btnPi)?.setOnClickListener { appendToExpression("π") }
        findViewById<Button>(R.id.btnFactorial)?.setOnClickListener { calculateFactorial() }
        findViewById<Button>(R.id.btnEE)?.setOnClickListener { appendToExpression("E") }
        findViewById<Button>(R.id.btnRand)?.setOnClickListener { insertRandom() }

        // Parentheses
        findViewById<Button>(R.id.btnLeftParen)?.setOnClickListener { appendToExpression("(") }
        findViewById<Button>(R.id.btnRightParen)?.setOnClickListener { appendToExpression(")") }

        // Duplicate basic functions (scientific layout only)
        findViewById<Button>(R.id.btnDeleteSci)?.setOnClickListener { deleteLastChar() }
        findViewById<Button>(R.id.btnACSci)?.setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnPercentSci)?.setOnClickListener { calculatePercent() }
        findViewById<Button>(R.id.btnDivideSci)?.setOnClickListener { appendToExpression("÷") }
    }

    private fun appendToExpression(value: String) {
        if (currentExpression == "0" && value != ".") {
            currentExpression = value
        } else {
            currentExpression += value
        }
        displayExpression.text = currentExpression
        evaluateLive()
    }

    private fun appendFunction(func: String) {
        currentExpression += func
        displayExpression.text = currentExpression
    }

    private fun deleteLastChar() {
        if (currentExpression.isNotEmpty() && currentExpression != "0") {
            currentExpression = currentExpression.dropLast(1)
            if (currentExpression.isEmpty()) {
                currentExpression = "0"
            }
            displayExpression.text = currentExpression
            evaluateLive()
        }
    }

    private fun clearAll() {
        currentExpression = "0"
        lastResult = ""
        displayExpression.text = "0"
        displayResult.text = ""
    }

    private fun calculatePercent() {
        try {
            val value = evaluateExpression(currentExpression)
            currentExpression = (value / 100).toString()
            displayExpression.text = currentExpression
            evaluateLive()
        } catch (e: Exception) {
            displayResult.text = "Error"
        }
    }

    private fun toggleSign() {
        try {
            val value = evaluateExpression(currentExpression)
            currentExpression = (-value).toString()
            displayExpression.text = currentExpression
            evaluateLive()
        } catch (e: Exception) {
            displayResult.text = "Error"
        }
    }

    private fun calculateInverse() {
        try {
            val value = evaluateExpression(currentExpression)
            if (value != 0.0) {
                currentExpression = (1.0 / value).toString()
                displayExpression.text = currentExpression
                evaluateLive()
            } else {
                displayResult.text = "Error"
            }
        } catch (e: Exception) {
            displayResult.text = "Error"
        }
    }

    private fun calculateFactorial() {
        try {
            val value = evaluateExpression(currentExpression).toInt()
            if (value in 0..20) {
                var result = 1L
                for (i in 2..value) result *= i
                currentExpression = result.toString()
                displayExpression.text = currentExpression
                evaluateLive()
            } else displayResult.text = "Error"
        } catch (e: Exception) {
            displayResult.text = "Error"
        }
    }

    private fun insertRandom() {
        val random = Math.random()
        currentExpression += String.format("%.6f", random)
        displayExpression.text = currentExpression
        evaluateLive()
    }

    private fun evaluateLive() {
        if (currentExpression.isEmpty() || currentExpression == "0") {
            displayResult.text = ""
            return
        }
        try {
            val result = evaluateExpression(currentExpression)
            displayResult.text = formatResult(result)
        } catch (_: Exception) {
            displayResult.text = ""
        }
    }

    private fun calculateResult() {
        if (currentExpression.isEmpty() || currentExpression == "0") return
        try {
            val result = evaluateExpression(currentExpression)
            lastResult = formatResult(result)
            displayResult.text = "= $lastResult"
            currentExpression = lastResult
        } catch (e: Exception) {
            displayResult.text = "Error"
        }
    }

    private fun evaluateExpression(expr: String): Double {
        var expression = expr
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
            .replace("π", Math.PI.toString())
            .replace("e", Math.E.toString())

        while (expression.isNotEmpty() && expression.last() in "+-*/^") {
            expression = expression.dropLast(1)
        }

        if (expression.isEmpty()) return 0.0
        return ExpressionBuilder(expression).build().evaluate()
    }

    private fun formatResult(result: Double): String {
        return when {
            result.isNaN() || result.isInfinite() -> "Error"
            result % 1.0 == 0.0 && Math.abs(result) < 1e10 ->
                result.toLong().toString()
            Math.abs(result) < 1e-6 || Math.abs(result) >= 1e10 ->
                String.format("%.6e", result)
            else ->
                String.format("%.10f", result).trimEnd('0').trimEnd('.')
        }
    }
}
