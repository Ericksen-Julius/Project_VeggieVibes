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
import com.example.project.data.entity.Sayur
import java.lang.Exception
import java.text.DecimalFormat
import java.util.Locale

data class carouselAdapter (
    private val listSayur: List<Sayur>
) : RecyclerView.Adapter<carouselAdapter.ListViewHolder>(){
    private lateinit var context: Context
    private lateinit var database: AppDatabase
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var nama = itemView.findViewById<TextView>(R.id.namaSayurCarousel)
        var totalSold = itemView.findViewById<TextView>(R.id.totalSoldCarousel)
        var position = itemView.findViewById<ImageView>(R.id.position)
        var imageCarousel = itemView.findViewById<ImageView>(R.id.imageCarousel)
        var desc = itemView.findViewById<TextView>(R.id.bacotSaja)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): carouselAdapter.ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.carousel_item,parent,false)
        context = parent.context
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        database = AppDatabase.getInstance(context)
        val sayur  = listSayur[position]
        val arrayOfDesc = arrayListOf<String>(
            "Experience the absolute best with our top-selling product, consistently chosen as the number one favorite among our customers.",
            "Discover excellence with our second top-selling item, celebrated for its exceptional quality and popularity among savvy shoppers.",
            "Explore our third best-selling product, a top choice known for its reliability and customer satisfaction, making it a consistent favorite.")
        val arrayofPosition = arrayListOf<String>(
            "medal.png",
            "medal_2.png",
            "medal_3.png")
        val bitmap: Bitmap? = sayur.gambar?.let { decodeImage(context, it) }
        bitmap?.let {
            holder.imageCarousel.setImageBitmap(bitmap)
        }
        holder.desc.text = arrayOfDesc[position]
        holder.nama.text = sayur.nama
        holder.totalSold.text = "${sayur.sold} items sold"
        val bitmap2: Bitmap? = decodeImage(context,arrayofPosition[position])
        bitmap2?.let {
            holder.imageCarousel.setImageBitmap(bitmap)
        }
        holder.position.setImageBitmap(bitmap2)


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
        return listSayur.size
    }




}