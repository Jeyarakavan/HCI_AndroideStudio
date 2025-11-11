package com.example.expensetracker

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.button.MaterialButton

class PieChartActivity : AppCompatActivity() {

    private lateinit var expensePieChart: PieChart
    private lateinit var trendLineChart: LineChart
    private lateinit var budgetBarChart: BarChart
    private lateinit var backButton: MaterialButton
    private lateinit var budgetMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart)

        // Initialize views
        expensePieChart = findViewById(R.id.expensePieChart)
        trendLineChart = findViewById(R.id.trendLineChart)
        budgetBarChart = findViewById(R.id.budgetBarChart)
        backButton = findViewById(R.id.backButton)
        budgetMessage = findViewById(R.id.budgetMessage)

        // Setup charts
        setupPieChart()
        setupLineChart()
        setupBudgetChart()

        // Back button
        backButton.setOnClickListener { onBackPressed() }
    }

    private fun setupPieChart() {
        val entries = listOf(
            PieEntry(60f, "Food"),
            PieEntry(30f, "Shopping"),
            PieEntry(10f, "Entertainment")
        )

        val dataSet = PieDataSet(entries, "Expense Categories").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
            valueTextColor = Color.WHITE
            valueTextSize = 12f
        }

        expensePieChart.apply {
            data = PieData(dataSet)
            description.isEnabled = false
            legend.isEnabled = true
            animateY(1000)
            invalidate()
        }
    }

    private fun setupLineChart() {
        val dates = listOf("1 Apr", "6 Apr", "12 Apr", "18 Apr", "24 Apr")
        val incomeValues = listOf(100f, 200f, 150f, 300f, 250f)
        val expenseValues = listOf(50f, 100f, 75f, 120f, 80f)

        // Income line
        val incomeEntries = dates.indices.map { i ->
            Entry(i.toFloat(), incomeValues[i])
        }

        // Expense line
        val expenseEntries = dates.indices.map { i ->
            Entry(i.toFloat(), expenseValues[i])
        }

        val incomeLine = LineDataSet(incomeEntries, "Income").apply {
            color = Color.GREEN
            lineWidth = 3f
            setCircleColor(Color.GREEN)
            circleRadius = 5f
        }

        val expenseLine = LineDataSet(expenseEntries, "Expense").apply {
            color = Color.RED
            lineWidth = 3f
            setCircleColor(Color.RED)
            circleRadius = 5f
        }

        trendLineChart.apply {
            data = LineData(incomeLine, expenseLine)
            description.isEnabled = false
            legend.isEnabled = true
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IndexAxisValueFormatter(dates)
                granularity = 1f
            }
            axisRight.isEnabled = false
            animateX(1000)
        }
    }

    private fun setupBudgetChart() {
        // Sample data - replace with your actual budget vs expense
        val categories = listOf("Food", "Shopping", "Entertainment")
        val budgets = listOf(100f, 150f, 50f)
        val expenses = listOf(60f, 30f, 10f)

        // Calculate spending percentage
        val percentages = expenses.mapIndexed { i, expense ->
            (expense / budgets[i] * 100).toInt()
        }

        // Set funny/angry message
        val avgPercentage = percentages.average().toInt()
        budgetMessage.text = when {
            avgPercentage < 20 -> "😂 Haha! Only $avgPercentage% spent!"
            avgPercentage < 50 -> "😊 Good job! $avgPercentage% spent"
            avgPercentage < 80 -> "😐 Careful! $avgPercentage% spent"
            else -> "😡 ANGRY! $avgPercentage% spent!!!"
        }

        // Bar entries
        val budgetEntries = categories.indices.map { i ->
            BarEntry(i.toFloat(), budgets[i])
        }

        val expenseEntries = categories.indices.map { i ->
            BarEntry(i.toFloat(), expenses[i])
        }

        val budgetSet = BarDataSet(budgetEntries, "Budget").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
            valueTextSize = 10f
        }

        val expenseSet = BarDataSet(expenseEntries, "Expense").apply {
            color = Color.RED
            valueTextColor = Color.BLACK
            valueTextSize = 10f
        }

        budgetBarChart.apply {
            data = BarData(budgetSet, expenseSet).apply {
                barWidth = 0.3f // Adjust bar width
            }
            description.isEnabled = false
            legend.isEnabled = true
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IndexAxisValueFormatter(categories)
                granularity = 1f
            }
            axisRight.isEnabled = false
            setFitBars(true)
            animateY(1000)
        }
    }
}