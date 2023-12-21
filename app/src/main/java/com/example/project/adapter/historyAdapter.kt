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
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

data class historyAdapter(
    private val listOrder: List<Order>
) : RecyclerView.Adapter<historyAdapter.ListViewHolder>(){
    private lateinit var context: Context // Added context property
    private lateinit var database : AppDatabase
    private lateinit var OnItemClickCallBack : onItemClickCallBack

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var namaToko: TextView = itemView.findViewById(R.id.namaToko)
        var waktukirim: TextView = itemView.findViewById(R.id.waktuOrder)
        var totalHarga: TextView = itemView.findViewById(R.id.totalharga)
        var waktusampai : TextView = itemView.findViewById(R.id.waktuSampai)

        init {
            itemView.setOnClickListener {
                OnItemClickCallBack.toDetail(position)
            }
        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: onItemClickCallBack){
        this.OnItemClickCallBack = onItemClickCallBack
    }

    interface onItemClickCallBack{
        fun toDetail(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_history2,parent,false)
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
        val id_sayur = listKeranjang[0].sayur_id
        val carisayur = id_sayur?.let { database.userDao().loadAllByIdsSayur(it)}
        val id_user = carisayur?.pemilik
        val caripemilik = database.userDao().loadAllByIds(id_user)
        val namatoko = caripemilik.namaToko
        var count = 1
        if(listKeranjang.count() > 1){
//            val id_sayur = listKeranjang[0].sayur_id
//            val carisayur = id_sayur?.let { database.userDao().loadAllByIdsSayur(it)}
//            val id_user = carisayur?.pemilik
//            val caripemilik = database.userDao().loadAllByIds(id_user)
//            val namatoko = caripemilik.namaToko

            for(x in 0..listKeranjang.count()-1){
                val id_sayur2 = listKeranjang[x].sayur_id
                val carisayur2 = id_sayur2?.let { database.userDao().loadAllByIdsSayur(it)}
                val id_user2 = carisayur2?.pemilik
                val caripemilik2 = database.userDao().loadAllByIds(id_user2)
                val tmp = caripemilik2.namaToko
                if(tmp != namatoko){
                    count++
                }

            }
        }
//        val id_sayur= listKeranjang[0].sayur_id
//        val carisayur = id_sayur?.let { database.userDao().loadAllByIdsSayur(it)}
//        val id_user = carisayur?.pemilik
//        val caripemilik = database.userDao().loadAllByIds(id_user)
        val formattedPrice = order.totalHarga?.let { formatDecimal(it) }
        if(count == 1){
            holder.namaToko.text = caripemilik.namaToko.toString()
        }else{
            holder.namaToko.text = "$count Asal Toko"
        }

        val pattern = "dd MMM yyyy, HH:mm"
        val sdf = SimpleDateFormat(pattern, Locale("id", "ID"))
        val time = order.waktuDatang
        val time2 = order.waktuSampai
        val formatteddate = sdf.format(time)
        holder.waktukirim.text = "Waktu Pesan: " + formatteddate.toString()
        val formatteddate2 = sdf.format(time2)
        holder.waktusampai.text = "Waktu Sampai: "+formatteddate2.toString()
        holder.totalHarga.text = "Rp. $formattedPrice"
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