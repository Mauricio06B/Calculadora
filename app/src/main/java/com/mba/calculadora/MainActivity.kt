package com.mba.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.mba.calculadora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    var concatenatedNumbers = "" // Variable para mantener la concatenación
    private lateinit var textViewResult: TextView // Referencia al TextView
    /*var FirstNumber = ""
    var SecondNumber = ""
    var currentOperation = ""*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa el TextView
        textViewResult = findViewById(R.id.txtPanel)

        binding.btnDeleteAll.setOnClickListener(this)
        binding.btnParenthesis.setOnClickListener(this)
        binding.btnParenthesisTwo.setOnClickListener(this)
        binding.btnDivision.setOnClickListener(this)
        binding.btnSeven.setOnClickListener(this)
        binding.btnEight.setOnClickListener(this)
        binding.btnNine.setOnClickListener(this)
        binding.btnMultiplication.setOnClickListener(this)
        binding.btnFour.setOnClickListener(this)
        binding.btnFive.setOnClickListener(this)
        binding.btnSix.setOnClickListener(this)
        binding.btnSubtraction.setOnClickListener(this)
        binding.btnOne.setOnClickListener(this)
        binding.btnTwo.setOnClickListener(this)
        binding.btnThree.setOnClickListener(this)
        binding.btnAddition.setOnClickListener(this)
        binding.btnZero.setOnClickListener(this)
        binding.btnSpot.setOnClickListener(this)
        binding.btnDelete.setOnClickListener(this)
        binding.btnEqual.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnZero -> {
                recibirNumero("0")
            }

            R.id.btnOne -> {
                recibirNumero("1")
            }

            R.id.btnTwo -> {
                recibirNumero("2")
            }

            R.id.btnThree -> {
                recibirNumero("3")
            }

            R.id.btnFour -> {
                recibirNumero("4")
            }

            R.id.btnFive -> {
                recibirNumero("5")
            }

            R.id.btnSix -> {
                recibirNumero("6")
            }

            R.id.btnSeven -> {
                recibirNumero("7")
            }

            R.id.btnEight -> {
                recibirNumero("8")
            }

            R.id.btnNine -> {
                recibirNumero("9")
            }

            R.id.btnAddition -> {
                recibirOperador("+")
            }

            R.id.btnSubtraction -> {
                recibirOperador("-")
            }

            R.id.btnMultiplication -> {
                recibirOperador("*")
            }

            R.id.btnDivision -> {
                recibirOperador("/")
            }

            R.id.btnEqual -> {
                val expr = binding.txtPanel.text.toString()
                val resultado = evaluar(expr)
                binding.txtPanel.text = resultado.toString()
            }

            R.id.btnParenthesis -> {
                recibirOperador("(")
            }

            R.id.btnParenthesisTwo -> {
                recibirOperador(")")
            }

            R.id.btnEqual -> {
                val expr = binding.txtPanel.text.toString()
                val resultado = evaluar(expr)
                binding.txtPanel.text = resultado.toString()
            }

            R.id.btnDelete -> {
                deleteNumber()
            }

            R.id.btnDeleteAll -> {
                deleteAll()
            }

        }
    }
    private fun recibirNumero(numero: String) {
        binding.txtPanel.append(numero)
    }

    private fun evaluar(expr: String): Double {
        val numeros = mutableListOf<Double>()
        val operadores = mutableListOf<Char>()

        var i = 0
        while (i < expr.length) {
            if (expr[i].isWhitespace()) {
                i++
                continue
            }

            if (expr[i].isDigit() || expr[i] == '.') {
                var buffer = ""
                while (i < expr.length && (expr[i].isDigit() || expr[i] == '.')) {
                    buffer += expr[i]
                    i++
                }
                numeros.add(buffer.toDouble())
            }

            else if (expr[i] == '(') {
                if (!verifyOperatorBeforeParentesis(expr, i)) {
                    return Double.NaN
                }
                operadores.add(expr[i])
                i++
            }
            else if (expr[i] == ')') {
                if (!verifyParentesis(operadores)) {
                    return Double.NaN
                }
                while (operadores.last() != '(') {
                    processOperation(numeros, operadores)
                }
                operadores.removeAt(operadores.size - 1) // remove '('
                i++
            }
            else if (expr[i] == '+' || expr[i] == '-' || expr[i] == '*' || expr[i] == '/') {
                if (!verifyOperator(expr, i)) {
                    return Double.NaN
                }
                while (operadores.isNotEmpty() && operadores.last() != '(' && priority(expr[i]) <= priority(
                        operadores.last()
                    )
                ) {
                    processOperation(numeros, operadores)
                }
                operadores.add(expr[i])
                i++
            }
        }
        if (!verifyParejaParentesis(operadores)) {
            return Double.NaN
        }

        while (operadores.isNotEmpty()) {
            processOperation(numeros, operadores)
        }

        return numeros.last()
    }
    private fun priority(op: Char): Int {
        return when (op) {
            '+' -> 1
            '-' -> 1
            '*' -> 2
            '/' -> 2
            else -> -1
        }
    }
    private fun processOperation(numeros: MutableList<Double>, operadores: MutableList<Char>) {
        if (numeros.size < 2 || operadores.isEmpty()) {
            Toast.makeText(this, "Error: Operación inválida", Toast.LENGTH_SHORT).show()
            return
        }

        val num1 = numeros.removeAt(numeros.size - 1)
        val num2 = numeros.removeAt(numeros.size - 1)

        val result = when (operadores.removeAt(operadores.size - 1)) {
            '+' -> num2 + num1
            '-' -> num2 - num1
            '*' -> num2 * num1
            '/' -> {
                if (num1 == 0.0) {
                    Toast.makeText(this, "Error: División por cero", Toast.LENGTH_SHORT).show()
                    return
                } else {
                    num2 / num1
                }
            }

            else -> 0.0
        }

        numeros.add(result)
    }
    private fun verifyOperatorBeforeParentesis(expr: String, i: Int): Boolean {
        if (i > 0 && expr[i - 1].isDigit() && expr[i + 1] != '-' && expr[i + 1] != '+') {
            Toast.makeText(
                this,
                "Error: Número seguido de paréntesis de apertura sin operador",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }
    private fun verifyParentesis(operadores: MutableList<Char>): Boolean {
        if (operadores.isEmpty() || !operadores.contains('(')) {
            Toast.makeText(
                this,
                "Error: Paréntesis de cierre sin paréntesis de apertura correspondiente",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }
    private fun verifyParejaParentesis(operadores: MutableList<Char>): Boolean {
        if (operadores.contains('(')) {
            Toast.makeText(
                this,
                "Error: Paréntesis de apertura sin paréntesis de cierre correspondiente",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }
    private fun verifyOperator(expr: String, i: Int): Boolean {
        if (i > 0 && "+-*/".contains(expr[i - 1])) {
            Toast.makeText(this, "Error: Dos operadores consecutivos", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun recibirOperador(operador: String) {
        binding.txtPanel.append(operador)
    }
    private fun deleteNumber() {
        val text = binding.txtPanel.text.toString()
        if (text.isNotEmpty()) {
            binding.txtPanel.text = text.substring(0, text.length - 1)
        }
    }

    private fun deleteAll() {
        binding.txtPanel.text = ""
    }
}
























    // Función onClick
    /*override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnZero -> appendNumber("0")
            R.id.btnOne -> appendNumber("1")
            R.id.btnTwo -> appendNumber("2")
            R.id.btnThree -> appendNumber("3")
            R.id.btnFour -> appendNumber("4")
            R.id.btnFive -> appendNumber("5")
            R.id.btnSix -> appendNumber("6")
            R.id.btnSeven -> appendNumber("7")
            R.id.btnEight -> appendNumber("8")
            R.id.btnNine -> appendNumber("9")
            R.id.btnParenthesisTwo -> (")")
            R.id.btnParenthesis -> ("(")

            R.id.btnDeleteAll -> ClearAll()
            R.id.btnDelete -> DeleteNumber()

            R.id.btnAddition -> handleOperation("+")
            R.id.btnSubtraction -> handleOperation("-")
            R.id.btnMultiplication -> handleOperation("*")
            R.id.btnDivision -> handleOperation("/")

            R.id.btnEqual -> calculateResult()
        }
    }

    //Funcion para coger los numeros
    private fun handleOperation(operation: String) {
        currentOperation = operation
        FirstNumber = binding.txtPanel.text.toString()
        binding.txtPanel.text = ""
    }

    //Funcion para calcular el resultado
    private fun calculateResult() {
        SecondNumber = binding.txtPanel.text.toString()

        if (FirstNumber.isNotEmpty() && SecondNumber.isNotEmpty() && currentOperation.isNotEmpty()) {
            val num1 = FirstNumber.toDouble()
            val num2 = SecondNumber.toDouble()
            var resultado = 0.0

            when (currentOperation) {
                "+" -> resultado = num1 + num2
                "-" -> resultado = num1 - num2
                "*" -> resultado = num1 * num2
                "/" -> resultado = num1 / num2
            }

            binding.txtPanel.text = resultado.toString()
        }
    }

    // Función para agregar números al TextView
    private fun appendNumber(number: String) {
        binding.txtPanel.text = binding.txtPanel.text.toString() + number
    }

    // Función para borrar todo
    private fun ClearAll() {
        concatenatedNumbers = ""
        textViewResult.text = ""
    }

    //Funcion para borrar uno
    private fun DeleteNumber() {
        val currentText = binding.txtPanel.text.toString()
        if (currentText.isNotEmpty()) {
            val newText = currentText.substring(0, currentText.length - 1)
            binding.txtPanel.text = newText
        }
    }*/


