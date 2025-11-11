package com.example.expensetracker

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.json.JSONArray

class InsightsActivity : AppCompatActivity() {

    private lateinit var incomeTable: TableLayout
    private lateinit var expenseTable: TableLayout
    private lateinit var transactions: MutableList<Transaction>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insights)

        // Initialize views
        incomeTable = findViewById(R.id.incomeTable)
        expenseTable = findViewById(R.id.expenseTable)
        val viewPieChartBtn = findViewById<Button>(R.id.viewPieChartBtn)

        // Set click listener for Pie Chart button
        viewPieChartBtn.setOnClickListener {
            startActivity(Intent(this, PieChartActivity::class.java))
        }

        // Load and display transactions
        sharedPreferences = getSharedPreferences("transactions_pref", MODE_PRIVATE)
        loadTransactions()
        populateTables()
    }

    private fun loadTransactions() {
        transactions = mutableListOf()
        val jsonString = sharedPreferences.getString("transactions", "[]")
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            transactions.add(
                Transaction(
                    label = obj.getString("label"),
                    amount = obj.getDouble("amount"),
                    category = obj.optString("category", "Other"),
                    date = obj.optString("date", "")
                )
            )
        }
    }

    private fun populateTables() {
        incomeTable.removeAllViews()
        expenseTable.removeAllViews()

        // Add Income Table Header
        val incomeHeader = TableRow(this).apply {
            addView(createHeaderTextView("Label"))
            addView(createHeaderTextView("Amount"))
        }
        incomeTable.addView(incomeHeader)

        // Add Income Rows
        transactions.filter { it.amount > 0 }.forEach { transaction ->
            val row = TableRow(this).apply {
                addView(createNormalTextView(transaction.label))
                addView(createAmountTextView("₹${transaction.amount}", R.color.green))
            }
            incomeTable.addView(row)
        }

        // Add Expense Table Header
        val expenseHeader = TableRow(this).apply {
            addView(createHeaderTextView("Label"))
            addView(createHeaderTextView("Amount"))
        }
        expenseTable.addView(expenseHeader)

        // Add Expense Rows
        transactions.filter { it.amount < 0 }.forEach { transaction ->
            val row = TableRow(this).apply {
                addView(createNormalTextView(transaction.label))
                addView(createAmountTextView("₹${-transaction.amount}", R.color.red))
            }
            expenseTable.addView(row)
        }
    }

    // Helper function to create header TextViews
    private fun createHeaderTextView(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 18f
            setPadding(16, 16, 16, 16)
            setTypeface(null, Typeface.BOLD)
        }
    }

    // Helper function to create normal TextViews
    private fun createNormalTextView(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 16, 16, 16)
        }
    }

    // Helper function to create amount TextViews with color
    private fun createAmountTextView(text: String, colorRes: Int): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 16, 16, 16)
            setTextColor(ContextCompat.getColor(this@InsightsActivity, colorRes))
        }
    }
}