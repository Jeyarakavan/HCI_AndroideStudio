//package com.example.expensetracker
//
//import android.content.SharedPreferences
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.github.mikephil.charting.charts.BarChart
//import com.github.mikephil.charting.charts.PieChart
//import com.github.mikephil.charting.components.AxisBase
//import com.github.mikephil.charting.components.XAxis
//import com.github.mikephil.charting.data.BarData
//import com.github.mikephil.charting.data.BarDataSet
//import com.github.mikephil.charting.data.BarEntry
//import com.github.mikephil.charting.data.Entry
//import com.github.mikephil.charting.data.PieData
//import com.github.mikephil.charting.data.PieDataSet
//import com.github.mikephil.charting.formatter.ValueFormatter
//
//class ChartActivity : AppCompatActivity() {
//
//    private lateinit var pieChart: PieChart
//    private lateinit var barChart: BarChart
//    private lateinit var budgetAmount: TextView
//    private lateinit var expenseAmount: TextView
//    private lateinit var sharedPreferences: SharedPreferences
//    private lateinit var transactions: List<Transaction>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_chart)
//
//        pieChart = findViewById(R.id.pieChart)
//        barChart = findViewById(R.id.barChart)
//        budgetAmount = findViewById(R.id.budgetAmount)
//        expenseAmount = findViewById(R.id.expenseAmount)
//        sharedPreferences = getSharedPreferences("transactions_pref", MODE_PRIVATE)
//
//        loadTransactions()
//
//        setupPieChart()
//        setupBarChart()
//        updateDashboard()
//    }
//
//    private fun loadTransactions() {
//        // Load transactions from SharedPreferences or wherever it's stored
//        transactions = listOf() // Replace with actual data loading logic
//    }
//
//    private fun setupPieChart() {
//        val expenseCategories = transactions.groupBy { it.category }
//        val pieEntries = mutableListOf<Entry>()
//        var i = 0f
//
//        for ((category, transactionsInCategory) in expenseCategories) {
//            val totalCategoryExpense = transactionsInCategory.sumOf { it.amount }
//            pieEntries.add(Entry(i++, totalCategoryExpense.toFloat()))
//        }
//
//        val pieDataSet = PieDataSet(pieEntries, "Expenses by Category")
//        pieDataSet.valueTextSize = 12f
//        val pieData = PieData(pieDataSet)
//        pieChart.data = pieData
//        pieChart.invalidate()
//    }
//
//    private fun setupBarChart() {
//        val incomeEntries = mutableListOf<BarEntry>()
//        val dateGroup = transactions.groupBy { it.selectedDate }
//
//        var i = 0f
//        for ((date, transactionList) in dateGroup) {
//            val totalIncome = transactionList.filter { it.amount > 0 }.sumOf { it.amount }
//            incomeEntries.add(BarEntry(i++, totalIncome.toFloat()))
//        }
//
//        val barDataSet = BarDataSet(incomeEntries, "Income by Date")
//        barDataSet.color = resources.getColor(R.color.green)
//        val barData = BarData(barDataSet)
//        barChart.data = barData
//        barChart.invalidate()
//    }
//
//    private fun updateDashboard() {
//        val totalExpenses = transactions.filter { it.amount < 0 }.sumOf { it.amount }
//        val budget = sharedPreferences.getFloat("budget", 0f).toDouble()
//
//        expenseAmount.text = "$ %.2f".format(-totalExpenses)
//        budgetAmount.text = "$ %.2f".format(budget)
//
//        if (-totalExpenses >= budget * 0.1) {
//            showFunnyMessage()
//        }
//    }
//
//    private fun showFunnyMessage() {
//        val currentExpense = -transactions.filter { it.amount < 0 }.sumOf { it.amount }
//        val budget = sharedPreferences.getFloat("budget", 0f).toDouble()
//        val message = when {
//            currentExpense >= budget * 0.1 -> "🚨 Hold on tight! You've spent over 10% of your budget! Time to stop splurging!"
//            else -> "You’re doing great! Keep it up!"
//        }
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//    }
//}
