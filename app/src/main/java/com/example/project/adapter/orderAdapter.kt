package com.example.project.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.data.AppDatabase
import com.example.project.data.entity.Order
import com.example.project.data.entity.Sayur
import java.lang.Exception
import java.text.DecimalFormat

data class orderAdapter(
    private val listOrder: List<Order>
) : RecyclerView.Adapter<orderAdapter.ListViewHolder>(){
    private lateinit var context: Context // Added context property
    private lateinit var database : AppDatabase
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var namaToko: TextView = itemView.findViewById(R.id.namaToko)
        var waktu: TextView = itemView.findViewById(R.id.waktuOrder)
        var totalHarga: TextView = itemView.findViewById(R.id.totalharga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_history,parent,false)
        context = parent.context
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        database = AppDatabase.getInstance(context)
        val order = listOrder[position]
        val formattedPrice = order.totalHarga?.let { formatDecimal(it) }
        val keranjangg = database.userDao().loadOrder(order.uid_user?:0)
        holder.namaToko.text = order.uidorder.toString()
        holder.waktu.text = order.waktu.toString()
        holder.totalHarga.text = "Rp. $formattedPrice"

    }

    fun formatDecimal(number: Int): String {
        val decimalFormat = DecimalFormat("#,##0")
        return decimalFormat.format(number)
    }


}