package com.example.expensetracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(
    private var transactions: MutableList<Transaction>,
    private val onEdit: (position: Int) -> Unit,
    private val onDelete: (position: Int) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label: TextView = view.findViewById(R.id.label)
        val amount: TextView = view.findViewById(R.id.amount)
        val edit: ImageView = view.findViewById(R.id.editButton)
        val delete: ImageView = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_item, parent, false)
        return TransactionViewHolder(view)
    }

    override fun getItemCount(): Int = transactions.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.label.text = transaction.label
        holder.amount.text = "$ %.2f".format(transaction.amount)
        holder.amount.setTextColor(
            if (transaction.amount >= 0) Color.parseColor("#388E3C")
            else Color.parseColor("#D32F2F")
        )

        holder.edit.setOnClickListener { onEdit(position) }
        holder.delete.setOnClickListener { onDelete(position) }
    }

    fun setData(newList: MutableList<Transaction>) {
        transactions = newList
        notifyDataSetChanged()
    }
}
