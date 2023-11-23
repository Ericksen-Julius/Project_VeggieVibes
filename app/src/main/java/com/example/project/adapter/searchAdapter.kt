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
import com.example.project.data.entity.Sayur
import java.lang.Exception
import java.text.DecimalFormat
import java.util.Locale


data class searchAdapter(
    private val listSayur: List<Sayur>
) : RecyclerView.Adapter<searchAdapter.ListViewHolder>(){
    private var filteredList: List<Sayur> = listSayur
    private lateinit var context: Context // Added context property
    private lateinit var database: AppDatabase // Added context property
    private lateinit var onItemClickCallBack: OnItemClickCallBack
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var gambarSayur: ImageView = itemView.findViewById(R.id.gambarSayurSearch)
        var namaSayur: TextView = itemView.findViewById(R.id.namaSayurSearch)
        var hargaSayur: TextView = itemView.findViewById(R.id.hargaSayurSearch)
        var _asalKota: TextView = itemView.findViewById(R.id.asalKotaSearch)
        var sold: TextView = itemView.findViewById(R.id.totalSold)
        var namaToko: TextView = itemView.findViewById(R.id.namaTokoSearch)
        var btnCart: Button = itemView.findViewById(R.id.addToCart)

    }

    interface OnItemClickCallBack{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): searchAdapter.ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_search,parent,false)
        context = parent.context
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        database = AppDatabase.getInstance(context)
        val sayur  = filteredList[position]
        if(sayur.stok!! > 0){
            val bitmap: Bitmap? = sayur.gambar?.let { decodeImage(context, it) }
            bitmap?.let {
                holder.gambarSayur.setImageBitmap(bitmap)
            }
            val dataPemilik = database.userDao().loadAllByIds(sayur.pemilik)
            holder.namaSayur.text = sayur.nama
            val formattedPrice = sayur.harga?.let { formatDecimal(it) }
            holder.hargaSayur.text = "Rp.${formattedPrice}"
            holder.sold.text = "${sayur.sold.toString()} terjual"
            holder.namaToko.text = dataPemilik.namaToko
            holder._asalKota.text = dataPemilik.asalKota
            holder.btnCart.setOnClickListener {
                this.onItemClickCallBack.onItemClick(position)
            }
        }

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
    override fun getItemCount(): Int {
        return filteredList.size
    }
    fun formatDecimal(number: Int): String {
        val decimalFormat = DecimalFormat("#,##0")
        return decimalFormat.format(number)
    }
    fun filter(query: String) {
        filteredList = if (query.isBlank()) {
            listSayur
        } else {
            listSayur.filter {
                it.nama?.lowercase(Locale.getDefault())?.contains(query.lowercase(Locale.getDefault()))!!
            }
        }
        notifyDataSetChanged()
    }


}