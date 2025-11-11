
package com.example.expensetracker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var transactionAdapter: TransactionAdapter
    private var transactions: MutableList<Transaction> = mutableListOf()

    private lateinit var balanceAmount: TextView
    private lateinit var budgetAmount: TextView
    private lateinit var expenseAmount: TextView
    private lateinit var transactionsRecyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var setBudgetButton: Button
    private lateinit var insightsButton: MaterialButton
    private lateinit var pieChartButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        balanceAmount = findViewById(R.id.balanceAmount)
        budgetAmount = findViewById(R.id.budgetAmount)
        expenseAmount = findViewById(R.id.expenseAmount)
        transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView)
        fab = findViewById(R.id.fab)
        pieChartButton =findViewById(R.id.pieChartButton)
        setBudgetButton = findViewById(R.id.setBudgetButton)
        insightsButton = findViewById(R.id.insightsButton)

        sharedPreferences = getSharedPreferences("transactions_pref", MODE_PRIVATE)
        loadTransactions()

        transactionAdapter = TransactionAdapter(
            transactions,
            onDelete = { position ->
                val deleted = transactions[position]
                transactions.removeAt(position)
                saveTransactions()
                transactionAdapter.setData(transactions)
                updateDashboard()

                Snackbar.make(transactionsRecyclerView, "Transaction deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        transactions.add(position, deleted)
                        saveTransactions()
                        transactionAdapter.setData(transactions)
                        updateDashboard()
                    }.show()
            },
            onEdit = { position ->
                val t = transactions[position]
                val intent = Intent(this, AddTransactionActivity::class.java)
                intent.putExtra("edit_position", position)
                intent.putExtra("label", t.label)
                intent.putExtra("amount", t.amount)
                intent.putExtra("category", t.category)
                intent.putExtra("date", t.date)
                startActivity(intent)
            }
        )

        transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionsRecyclerView.adapter = transactionAdapter

        insightsButton.setOnClickListener {
            startActivity(Intent(this, InsightsActivity::class.java))
        }


        pieChartButton.setOnClickListener {
            startActivity(Intent(this, PieChartActivity::class.java))
        }

        fab.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
        }

        setBudgetButton.setOnClickListener {
            showBudgetDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        loadTransactions()
        transactionAdapter.setData(transactions)
        updateDashboard()
    }

    private fun loadTransactions() {
        transactions.clear()
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

    private fun saveTransactions() {
        val jsonArray = JSONArray()
        for (t in transactions) {
            val obj = JSONObject().apply {
                put("label", t.label)
                put("amount", t.amount)
                put("category", t.category)
                put("date", t.date)
            }
            jsonArray.put(obj)
        }
        sharedPreferences.edit().putString("transactions", jsonArray.toString()).apply()
    }

    private fun updateDashboard() {
        val totalAmount = transactions.sumOf { it.amount }
        val budget = sharedPreferences.getFloat("budget", 0f).toDouble()
        val expenseTotal = transactions.filter { it.amount < 0 }.sumOf { it.amount }

        balanceAmount.text = "$ %.2f".format(totalAmount)
        budgetAmount.text = "$ %.2f".format(budget)
        expenseAmount.text = "$ %.2f".format(-expenseTotal)

        if (-expenseTotal >= budget * 0.1) {
            Toast.makeText(this, "⚠️ Expense exceeded 10% of your budget!", Toast.LENGTH_LONG).show()
        }
    }

    private fun showBudgetDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Monthly Budget")

        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        builder.setView(input)

        builder.setPositiveButton("Set") { _, _ ->
            val budget = input.text.toString().toDoubleOrNull() ?: 0.0
            saveBudget(budget)
        }
        builder.setNegativeButton("Cancel", null)

        builder.show()
    }

    private fun saveBudget(budget: Double) {
        sharedPreferences.edit().putFloat("budget", budget.toFloat()).apply()
        updateDashboard()
    }
}
