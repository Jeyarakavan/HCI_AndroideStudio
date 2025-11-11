package com.example.expensetracker

data class Transaction(
    val label: String,
    val amount: Double,
    val category: String,
    val date: String // Ensure date is included here
)
