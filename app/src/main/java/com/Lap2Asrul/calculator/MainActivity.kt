package com.hadirahimi.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.hadirahimi.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // binding
    private lateinit var binding: ActivityMainBinding

    // variables
    private var firstNumber = ""
    private var currentNumber = ""
    private var currentOperator = ""
    private var result = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NoLimitScreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        // initViews
        binding.apply {
            // get all buttons
            layoutMain.children.filterIsInstance<Button>().forEach { button ->
                // buttons click listener
                button.setOnClickListener {
                    // get clicked button text
                    val buttonText = button.text.toString()
                    when {
                        buttonText.matches(Regex("[0-9]")) -> {
                            if (currentOperator.isEmpty()) {
                                firstNumber += buttonText
                                tvResult.text = firstNumber
                            } else {
                                currentNumber += buttonText
                                tvResult.text = currentNumber
                            }
                        }
                        buttonText.matches(Regex("[+\\-*/]")) -> {
                            if (currentNumber.isNotEmpty()) {
                                // handle the previous calculation
                                if (firstNumber.isNotEmpty() && currentOperator.isNotEmpty()) {
                                    tvFormula.text = "$firstNumber$currentOperator$currentNumber"
                                    result = evaluateExpression(firstNumber, currentNumber, currentOperator)
                                    firstNumber = result
                                    tvResult.text = result
                                    currentNumber = ""
                                }
                            }
                            // set the new operator
                            currentOperator = buttonText
                        }
                        buttonText == "=" -> {
                            if (currentNumber.isNotEmpty() && currentOperator.isNotEmpty()) {
                                tvFormula.text = "$firstNumber$currentOperator$currentNumber"
                                result = evaluateExpression(firstNumber, currentNumber, currentOperator)
                                firstNumber = result
                                tvResult.text = result
                                currentOperator = "" // reset operator after calculation
                                currentNumber = ""
                            }
                        }
                        buttonText == "." -> {
                            if (currentOperator.isEmpty()) {
                                if (!firstNumber.contains(".")) {
                                    if (firstNumber.isEmpty()) firstNumber += "0$buttonText"
                                    else firstNumber += buttonText
                                    tvResult.text = firstNumber
                                }
                            } else {
                                if (!currentNumber.contains(".")) {
                                    if (currentNumber.isEmpty()) currentNumber += "0$buttonText"
                                    else currentNumber += buttonText
                                    tvResult.text = currentNumber
                                }
                            }
                        }
                        buttonText == "C" -> {
                            currentNumber = ""
                            firstNumber = ""
                            currentOperator = ""
                            tvResult.text = "0"
                            tvFormula.text = ""
                        }
                        buttonText == "eraser" -> {
                            when {
                                currentOperator.isEmpty() -> {
                                    if (firstNumber.isNotEmpty()) {
                                        firstNumber = firstNumber.substring(0, firstNumber.length - 1)
                                        tvResult.text = if (firstNumber.isEmpty()) "0" else firstNumber
                                    }
                                }
                                currentNumber.isNotEmpty() -> {
                                    currentNumber = currentNumber.substring(0, currentNumber.length - 1)
                                    tvResult.text = if (currentNumber.isEmpty()) "0" else currentNumber
                                }
                                else -> {
                                    currentOperator = ""
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    // functions
    private fun evaluateExpression(firstNumber: String, secondNumber: String, operator: String): String {
        val num1 = firstNumber.toDouble()
        val num2 = secondNumber.toDouble()
        return when (operator) {
            "+" -> (num1 + num2).toString()
            "-" -> (num1 - num2).toString()
            "*" -> (num1 * num2).toString()
            "/" -> (num1 / num2).toString()
            else -> ""
        }
    }
}
