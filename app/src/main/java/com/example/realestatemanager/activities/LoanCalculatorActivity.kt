package com.example.realestatemanager.activities
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.realestatemanager.R
import kotlin.math.pow
class LoanCalculatorActivity:Fragment(R.layout.loan_calculator_activity){
    private lateinit var etLoanAmount: EditText
    private lateinit var etInterestRate: EditText
    private lateinit var etLoanTerm: EditText
    private lateinit var btnCalculate: Button
    private lateinit var tvResult: TextView
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etLoanAmount =requireView().findViewById(R.id.etLoanAmount)
        etInterestRate = requireView().findViewById(R.id.etInterestRate)
        etLoanTerm =requireView().findViewById(R.id.etLoanTerm)
        btnCalculate =requireView().findViewById(R.id.btnCalculate)
        tvResult =requireView().findViewById(R.id.tvResult)
        btnCalculate.setOnClickListener {
            val loanAmount = etLoanAmount.text.toString().toDoubleOrNull() ?: 0.0
            val annualInterestRate = etInterestRate.text.toString().toDoubleOrNull() ?: 0.0
            val loanTermInYears = etLoanTerm.text.toString().toDoubleOrNull() ?: 0.0
            val loanTermInMonths = loanTermInYears * 12
            val monthlyInterestRate = annualInterestRate / 12 / 100
            val monthlyPayment = (loanAmount * monthlyInterestRate) / (1 - (1 + monthlyInterestRate).pow(-loanTermInMonths))
            tvResult.text = "Monthly Payment: \$%.2f".format(monthlyPayment)
        }
    }
}
