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
import com.example.project.data.entity.Sayur
import java.lang.Exception
import java.text.DecimalFormat

data class sayurAdapter(
    private val listSayur: List<Sayur>
) : RecyclerView.Adapter<sayurAdapter.ListViewHolder>(){
    private lateinit var OnItemClickCallBack : onItemClickCallBack
    private lateinit var context: Context // Added context property
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var gambarSayur: ImageView = itemView.findViewById(R.id.gambarSayur)
        var namaSayur: TextView = itemView.findViewById(R.id.namaSayur)
        var hargaSayur: TextView = itemView.findViewById(R.id.hargaSayur)
        var buttonDialog : ImageView = itemView.findViewById(R.id.menuIcon)
//        init{
//            itemView.setOnClickListener {
//                OnItemClickCallBack.onItemClick(layoutPosition)
//            }
//        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: onItemClickCallBack){
        this.OnItemClickCallBack = onItemClickCallBack
    }

    interface onItemClickCallBack{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_vegetables,parent,false)
        context = parent.context
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listSayur.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val sayur = listSayur[position]
        val bitmap: Bitmap? = sayur.gambar?.let { decodeImage(context, it) }
        bitmap?.let {
            holder.gambarSayur.setImageBitmap(bitmap)
        }
        val formattedPrice = sayur.harga?.let { formatDecimal(it) }
        holder.namaSayur.text = sayur.nama
        holder.hargaSayur.text = "Rp. $formattedPrice"
        holder.buttonDialog.setOnClickListener {
            OnItemClickCallBack.onItemClick(position)
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
    fun formatDecimal(number: Int): String {
        val decimalFormat = DecimalFormat("#,##0")
        return decimalFormat.format(number)
    }

}