package com.example.project.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.data.AppDatabase
import com.example.project.data.entity.Keranjang
import com.example.project.data.entity.Order
import java.lang.Exception
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.util.Date

data class orderAdapter(
    private val listOrder: List<Order>
) : RecyclerView.Adapter<orderAdapter.ListViewHolder>(){
    private lateinit var context: Context // Added context property
    private lateinit var database : AppDatabase
    private lateinit var OnItemClickCallBack : onItemClickCallBack

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var namaToko: TextView = itemView.findViewById(R.id.namaToko)
        var waktu: TextView = itemView.findViewById(R.id.waktuOrder)
        var totalHarga: TextView = itemView.findViewById(R.id.totalharga)
        var btnDone : Button = itemView.findViewById(R.id.btnDone)
    }

    fun setOnItemClickCallBack(onItemClickCallBack: onItemClickCallBack){
        this.OnItemClickCallBack = onItemClickCallBack
    }

    interface onItemClickCallBack{
        fun onItemDone(position: Int)
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
        val listKeranjang : List<Keranjang> = listOrder[position].keranjang
        val id_sayur= listKeranjang[0].sayur_id
        val carisayur = id_sayur?.let { database.userDao().loadAllByIdsSayur(it)}
        val id_user = carisayur?.pemilik
        val caripemilik = database.userDao().loadAllByIds(id_user)
        val formattedPrice = order.totalHarga?.let { formatDecimal(it) }
        holder.namaToko.text = caripemilik.namaToko.toString()
        holder.waktu.text = order.waktuDatang.toString()
        holder.totalHarga.text = "Rp. $formattedPrice"
        holder.btnDone.setOnClickListener {
            this.OnItemClickCallBack.onItemDone(position)
        }
    }

    fun formatDecimal(number: Int): String {
        val decimalFormat = DecimalFormat("#,##0")
        return decimalFormat.format(number)
    }

    fun formatDate(datee: Date){
        var formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
        var formattedDate = datee.toString()
        return
    }


}