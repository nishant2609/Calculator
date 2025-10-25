//package com.example.calculator
//
//import net.objecthunter.exp4j.ExpressionBuilder
//import kotlin.math.*
//
///**
// * CalculatorEngine - Optional enhanced calculator logic
// * This provides additional functionality beyond the basic MainActivity implementation
// */
//class CalculatorEngine {
//
//    private var memoryValue: Double = 0.0
//    private var isRadianMode: Boolean = true
//
//    companion object {
//        private const val MAX_DISPLAY_LENGTH = 15
//    }
//
//    /**
//     * Evaluate a mathematical expression
//     */
//    fun evaluate(expression: String): Result<Double> {
//        return try {
//            if (expression.isEmpty()) {
//                return Result.failure(Exception("Empty expression"))
//            }
//
//            val processed = preprocessExpression(expression)
//            val result = ExpressionBuilder(processed)
//                .build()
//                .evaluate()
//
//            if (result.isNaN() || result.isInfinite()) {
//                Result.failure(Exception("Invalid result"))
//            } else {
//                Result.success(result)
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    /**
//     * Preprocess expression to handle special cases
//     */
//    private fun preprocessExpression(expr: String): String {
//        var expression = expr
//            .replace("×", "*")
//            .replace("÷", "/")
//            .replace("−", "-")
//            .replace("π", Math.PI.toString())
//            .replace("e", Math.E.toString())
//
//        // Handle implicit multiplication (e.g., 2π -> 2*π)
//        expression = expression.replace(Regex("(\\d)([a-zA-Z(])"), "$1*$2")
//        expression = expression.replace(Regex("([)])([\\d(])"), "$1*$2")
//
//        return expression
//    }
//
//    /**
//     * Format result for display
//     */
//    fun formatResult(value: Double): String {
//        return when {
//            value.isNaN() -> "Error"
//            value.isInfinite() -> if (value > 0) "∞" else "-∞"
//            value % 1.0 == 0.0 && abs(value) < 1e10 -> {
//                value.toLong().toString()
//            }
//            abs(value) < 1e-6 || abs(value) >= 1e10 -> {
//                String.format("%.6e", value)
//            }
//            else -> {
//                val formatted = String.format("%.10f", value)
//                    .trimEnd('0')
//                    .trimEnd('.')
//
//                if (formatted.length > MAX_DISPLAY_LENGTH) {
//                    String.format("%.6e", value)
//                } else {
//                    formatted
//                }
//            }
//        }
//    }
//
//    /**
//     * Calculate factorial
//     */
//    fun factorial(n: Int): Long {
//        if (n < 0) throw IllegalArgumentException("Factorial of negative number")
//        if (n > 20) throw IllegalArgumentException("Factorial too large")
//
//        var result = 1L
//        for (i in 2..n) {
//            result *= i
//        }
//        return result
//    }
//
//    /**
//     * Convert degrees to radians
//     */
//    fun degreesToRadians(degrees: Double): Double {
//        return Math.toRadians(degrees)
//    }
//
//    /**
//     * Convert radians to degrees
//     */
//    fun radiansToDegrees(radians: Double): Double {
//        return Math.toDegrees(radians)
//    }
//
//    /**
//     * Calculate sine (handles both radians and degrees)
//     */
//    fun sin(value: Double): Double {
//        return if (isRadianMode) {
//            kotlin.math.sin(value)
//        } else {
//            kotlin.math.sin(degreesToRadians(value))
//        }
//    }
//
//    /**
//     * Calculate cosine (handles both radians and degrees)
//     */
//    fun cos(value: Double): Double {
//        return if (isRadianMode) {
//            kotlin.math.cos(value)
//        } else {
//            kotlin.math.cos(degreesToRadians(value))
//        }
//    }
//
//    /**
//     * Calculate tangent (handles both radians and degrees)
//     */
//    fun tan(value: Double): Double {
//        return if (isRadianMode) {
//            kotlin.math.tan(value)
//        } else {
//            kotlin.math.tan(degreesToRadians(value))
//        }
//    }
//
//    /**
//     * Calculate inverse sine
//     */
//    fun asin(value: Double): Double {
//        val result = kotlin.math.asin(value)
//        return if (isRadianMode) result else radiansToDegrees(result)
//    }
//
//    /**
//     * Calculate inverse cosine
//     */
//    fun acos(value: Double): Double {
//        val result = kotlin.math.acos(value)
//        return if (isRadianMode) result else radiansToDegrees(result)
//    }
//
//    /**
//     * Calculate inverse tangent
//     */
//    fun atan(value: Double): Double {
//        val result = kotlin.math.atan(value)
//        return if (isRadianMode) result else radiansToDegrees(result)
//    }
//
//    /**
//     * Calculate nth root
//     */
//    fun nthRoot(value: Double, n: Double): Double {
//        return value.pow(1.0 / n)
//    }
//
//    /**
//     * Calculate percentage
//     */
//    fun percentage(value: Double, percent: Double): Double {
//        return value * (percent / 100.0)
//    }
//
//    /**
//     * Memory operations
//     */
//    fun memoryClear() {
//        memoryValue = 0.0
//    }
//
//    fun memoryAdd(value: Double) {
//        memoryValue += value
//    }
//
//    fun memorySubtract(value: Double) {
//        memoryValue -= value
//    }
//
//    fun memoryRecall(): Double {
//        return memoryValue
//    }
//
//    fun memoryStore(value: Double) {
//        memoryValue = value
//    }
//
//    /**
//     * Toggle between Radian and Degree mode
//     */
//    fun toggleAngleMode() {
//        isRadianMode = !isRadianMode
//    }
//
//    fun isRadianMode(): Boolean {
//        return isRadianMode
//    }
//
//    fun getAngleMode(): String {
//        return if (isRadianMode) "Rad" else "Deg"
//    }
//
//    /**
//     * Validate expression for basic syntax errors
//     */
//    fun validateExpression(expression: String): Boolean {
//        if (expression.isEmpty()) return true
//
//        var openParentheses = 0
//        var lastChar = ' '
//
//        for (char in expression) {
//            when (char) {
//                '(' -> openParentheses++
//                ')' -> {
//                    openParentheses--
//                    if (openParentheses < 0) return false
//                }
//                in "+-×÷" -> {
//                    if (lastChar in "+-×÷") return false
//                }
//            }
//            lastChar = char
//        }
//
//        return openParentheses == 0
//    }
//
//    /**
//     * Check if expression ends with operator
//     */
//    fun endsWithOperator(expression: String): Boolean {
//        if (expression.isEmpty()) return false
//        val lastChar = expression.last()
//        return lastChar in "+-×÷^"
//    }
//
//    /**
//     * Smart parentheses closing
//     */
//    fun getSmartClosingParentheses(expression: String): String {
//        var openCount = 0
//        for (char in expression) {
//            when (char) {
//                '(' -> openCount++
//                ')' -> openCount--
//            }
//        }
//        return ")".repeat(maxOf(0, openCount))
//    }
//}
//
///**
// * Extension function to safely convert string to double
// */
//fun String.toDoubleOrNull(): Double? {
//    return try {
//        this.toDouble()
//    } catch (e: NumberFormatException) {
//        null
//    }
//}