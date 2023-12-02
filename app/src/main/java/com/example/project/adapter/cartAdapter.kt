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
import java.lang.Exception
import java.text.DecimalFormat
import java.util.Locale

data class cartAdapter (
    private val listKeranjang: List<Keranjang>
) : RecyclerView.Adapter<cartAdapter.ListViewHolder>(){
    private lateinit var context: Context // Added context property
    private lateinit var database: AppDatabase // Added context property
    private lateinit var onItemClickCallBack: OnItemClickCallBack
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var gambarSayur: ImageView = itemView.findViewById(R.id.imageKeranjang)
        var _namaToko: TextView = itemView.findViewById(R.id.namaToko)
        var hargaSayur: TextView = itemView.findViewById(R.id.hargaBarang)
        var namaSayur: TextView = itemView.findViewById(R.id.namaBarang)
        var deleteCart: ImageView = itemView.findViewById(R.id.deleteCart)
        var increment: Button = itemView.findViewById(R.id.incrementButton)
        var decrement: Button = itemView.findViewById(R.id.decrementButton)
        var textCount: TextView = itemView.findViewById(R.id.counterTextView)

    }

    interface OnItemClickCallBack{
        fun deleteCart(position: Int)
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartAdapter.ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart,parent,false)
        context = parent.context
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        database = AppDatabase.getInstance(context)
        val keranjang  = listKeranjang[position]
        val idSayur = keranjang.sayur_id
        val dataSayur = database.userDao().loadAllByIdsSayur(idSayur!!)
        val dataPenjual = database.userDao().loadAllByIds(dataSayur.pemilik)
        val bitmap: Bitmap? = dataSayur.gambar?.let { decodeImage(context, it) }
        bitmap?.let {
            holder.gambarSayur.setImageBitmap(bitmap)
        }
        holder._namaToko.text = dataPenjual.namaToko
        holder.namaSayur.text = dataSayur.nama
        val totalHarga = keranjang.count?.let { dataSayur.harga?.times(it) }
        val formattedPrice = totalHarga?.let { formatDecimal(it) }
        holder.hargaSayur.text = "Rp.${formattedPrice}"
        holder.increment.setOnClickListener {
            holder.textCount.text = (holder.textCount.text.toString().toInt()+1).toString()
            database.userDao().updateKeranjang(
                Keranjang(
                keranjang.uidKeranjang,
                idSayur,
                keranjang.user_id,
                holder.textCount.text.toString().toInt()
                )
            )
        }
        holder.decrement.setOnClickListener {
            if (holder.textCount.text.toString().toInt() > 0){
                holder.textCount.text = (holder.textCount.text.toString().toInt()-1).toString()
                database.userDao().updateKeranjang(
                    Keranjang(
                        keranjang.uidKeranjang,
                        idSayur,
                        keranjang.user_id,
                        holder.textCount.text.toString().toInt()
                    )
                )
            }
        }
        holder.deleteCart.setOnClickListener {
            this.onItemClickCallBack.deleteCart(position)
        }
        holder.textCount.text = keranjang.count.toString()

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
        return listKeranjang.size
    }
    fun formatDecimal(number: Int): String {
        val decimalFormat = DecimalFormat("#,##0")
        return decimalFormat.format(number)
    }



}