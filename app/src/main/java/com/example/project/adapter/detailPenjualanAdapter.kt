package com.example.project.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.data.AppDatabase
import com.example.project.data.entity.Penjualan
import java.lang.Exception
import java.text.DecimalFormat

data class detailPenjualanAdapter(
    private val listKeranjang: MutableList<Penjualan>
) : RecyclerView.Adapter<detailPenjualanAdapter.ListViewHolder>(){
    private lateinit var context: Context // Added context property
    private lateinit var database : AppDatabase
    private lateinit var OnItemClickCallBack : onItemClickCallBack

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var namaUser = itemView.findViewById<TextView>(R.id.namaUser)
        var jumlahBeli = itemView.findViewById<TextView>(R.id.jumlahBeli)
    }

    fun setOnItemClickCallBack(onItemClickCallBack: onItemClickCallBack){
        this.OnItemClickCallBack = onItemClickCallBack
    }

    interface onItemClickCallBack{

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_history_penjualan,parent,false)
        context = parent.context
        return ListViewHolder(view)
    }


    override fun getItemCount(): Int {
        return listKeranjang.count()
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        database = AppDatabase.getInstance(context)
        val keranjang = listKeranjang[position]
        holder.namaUser.text = database.userDao().loadAllByIds(keranjang.user_id).fullName
        holder.jumlahBeli.text = keranjang.count.toString()
    }

    fun formatDecimal(number: Int): String {
        val decimalFormat = DecimalFormat("#,##0")
        return decimalFormat.format(number)
    }

    private fun decodeImage(context: Context, filename: String): Bitmap? {
        return try {
            val inputStream = context.assets?.open(filename)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception){
            e.printStackTrace()
            null
        }
    }


}