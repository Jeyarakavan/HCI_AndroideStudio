package com.example.expensetracker

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SetBudgetActivity : AppCompatActivity() {

    private lateinit var budgetEditText: EditText
    private lateinit var saveBudgetButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_budget)

        budgetEditText = findViewById(R.id.budgetEditText)
        saveBudgetButton = findViewById(R.id.saveBudgetButton)
        sharedPreferences = getSharedPreferences("transactions_pref", MODE_PRIVATE)

        saveBudgetButton.setOnClickListener {
            val budget = budgetEditText.text.toString().toDoubleOrNull()
            if (budget != null && budget > 0) {
                sharedPreferences.edit().putFloat("monthly_budget", budget.toFloat()).apply()
                Toast.makeText(this, "Budget Set: ₹$budget", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Enter valid budget", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
