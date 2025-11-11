package com.example.expensetracker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var labelEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var datePicker: DatePicker
    private lateinit var saveButton: Button
    private lateinit var typeSpinner: Spinner

    private lateinit var sharedPreferences: SharedPreferences
    private var editPosition: Int = -1
    private var transactions: MutableList<Transaction> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        labelEditText = findViewById(R.id.labelEditText)
        amountEditText = findViewById(R.id.amountEditText)
        categorySpinner = findViewById(R.id.categorySpinner)
        datePicker = findViewById(R.id.datePicker)
        saveButton = findViewById(R.id.saveButton)
        typeSpinner = findViewById(R.id.typeSpinner)

        sharedPreferences = getSharedPreferences("transactions_pref", Context.MODE_PRIVATE)

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

        val typeOptions = listOf("Expense", "Income")
        typeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, typeOptions)

        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = typeOptions[position]
                val categories = if (selected == "Expense") {
                    listOf("Food", "Shopping", "Medicine", "Other")
                } else {
                    listOf("Salary", "Freelance", "Investment", "Other")
                }
                categorySpinner.adapter = ArrayAdapter(this@AddTransactionActivity, android.R.layout.simple_spinner_dropdown_item, categories)
            }

            override fun onNothingSelected(p0: AdapterView<*>) {}
        }

        editPosition = intent.getIntExtra("edit_position", -1)
        if (editPosition != -1) {
            labelEditText.setText(intent.getStringExtra("label"))
            amountEditText.setText(intent.getDoubleExtra("amount", 0.0).toString())
            val category = intent.getStringExtra("category") ?: "Other"
            val date = intent.getStringExtra("date") ?: ""
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            if (date.isNotEmpty()) {
                calendar.time = sdf.parse(date) ?: Date()
                datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            }
        }

        saveButton.setOnClickListener {
            val label = labelEditText.text.toString()
            val amountText = amountEditText.text.toString()
            val category = categorySpinner.selectedItem.toString()
            val type = typeSpinner.selectedItem.toString()
            if (label.isBlank() || amountText.isBlank()) return@setOnClickListener

            val amount = amountText.toDouble().let { if (type == "Expense") -it else it }

            val day = datePicker.dayOfMonth
            val month = datePicker.month
            val year = datePicker.year
            val date = String.format("%04d-%02d-%02d", year, month + 1, day)

            val transaction = Transaction(label, amount, category, date)
            if (editPosition != -1) {
                transactions[editPosition] = transaction
            } else {
                transactions.add(transaction)
            }

            val jsonArray = JSONArray()
            for (t in transactions) {
                val obj = JSONObject()
                obj.put("label", t.label)
                obj.put("amount", t.amount)
                obj.put("category", t.category)
                obj.put("date", t.date)
                jsonArray.put(obj)
            }

            sharedPreferences.edit().putString("transactions", jsonArray.toString()).apply()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
