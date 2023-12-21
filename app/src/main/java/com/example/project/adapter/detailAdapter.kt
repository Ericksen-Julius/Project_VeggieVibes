package com.example.project.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.ETC1.decodeImage
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
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

data class detailAdapter(
    private val listOrder: List<Order>
) : RecyclerView.Adapter<detailAdapter.ListViewHolder>(){
    private lateinit var context: Context // Added context property
    private lateinit var database : AppDatabase
    private lateinit var OnItemClickCallBack : onItemClickCallBack

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var namaToko: TextView = itemView.findViewById(R.id.detailNamaToko)
        var namaSayur : TextView = itemView.findViewById(R.id.detailNamaSayur)
        var jumlahxharga: TextView = itemView.findViewById(R.id.jumlahxhargaDetail)
        var subtotal : TextView = itemView.findViewById(R.id.subtotal)
        var gambar : ImageView = itemView.findViewById(R.id.gambarDetail)
        var stringgambar : String = ""
    }

    fun setOnItemClickCallBack(onItemClickCallBack: onItemClickCallBack){
        this.OnItemClickCallBack = onItemClickCallBack
    }

    interface onItemClickCallBack{

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_detail_order,parent,false)
        context = parent.context
        return ListViewHolder(view)
    }


    override fun getItemCount(): Int {
        return listOrder[0].keranjang.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        database = AppDatabase.getInstance(context)
        val listKeranjang : List<Keranjang> = listOrder[0].keranjang
        val id_sayur = listKeranjang[position].sayur_id
        val carisayur = id_sayur?.let { database.userDao().loadAllByIdsSayur(it)}
        val id_user = carisayur?.pemilik
        val caripemilik = database.userDao().loadAllByIds(id_user)
        val namatoko = caripemilik.namaToko
        val namaSayur = carisayur?.nama
        val jumlah = listKeranjang[position].count
        val harga = carisayur?.harga
        val subtotal = jumlah?.times(harga!!)
        val bitmap: Bitmap? = carisayur?.gambar?.let { decodeImage(context, it) }
        bitmap?.let {
            holder.gambar.setImageBitmap(bitmap)
        }
        holder.namaToko.text = namatoko
        holder.namaSayur.text = namaSayur
        val harga2 = harga?.let { formatDecimal(it) }
        holder.jumlahxharga.text = "$jumlah X Rp. $harga2"
        val formattedsubtotal = subtotal?.let { formatDecimal(it) }
        holder.subtotal.text = "Rp. $formattedsubtotal"
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