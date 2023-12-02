package com.example.project

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.project.data.AppDatabase
import java.lang.Exception
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [detailSayur.newInstance] factory method to
 * create an instance of this fragment.
 */
class detailSayur : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var nama : TextView
    private lateinit var harga : TextView
    private lateinit var berat : TextView
    private lateinit var gambar : ImageView
    private lateinit var sold : TextView
    private lateinit var stok : TextView
    private lateinit var database : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_sayur, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val getIdSayur = arguments?.getInt("sayurId",-1)
        (super.requireActivity() as Home).setTitle("Detail sayuran")
        nama = view.findViewById(R.id.sayurNama)
        harga = view.findViewById(R.id.sayurHarga)
        berat = view.findViewById(R.id.sayurBerat)
        sold = view.findViewById(R.id.sayurSold)
        stok = view.findViewById(R.id.sayurStok)
        gambar = view.findViewById(R.id.sayurImage)
        database = activity?.let { AppDatabase.getInstance(it.applicationContext) }!!
        val data = getIdSayur?.let { database.userDao().loadAllByIdsSayur(it) }
        val formattedPrice = data?.harga?.let { formatDecimal(it) }
        nama.text = "Nama : " + data?.nama
        harga.text = "Harga: $formattedPrice"
        berat.text = "Berat: " + data?.berat.toString()
        sold.text = "Sold: " + data?.sold.toString()
        stok.text = "Stok: " + data?.stok.toString()
        val bitmap: Bitmap? = data?.gambar?.let { getImage(requireContext(), it) }
        bitmap?.let {
            gambar.setImageBitmap(bitmap)
        }

    }
    fun formatDecimal(number: Int): String {
        val decimalFormat = DecimalFormat("#,##0")
        return decimalFormat.format(number)
    }
    private fun getImage(context: Context, filename: String): Bitmap? {
        return try {
            val inputStream = context.assets?.open(filename)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment detailSayur.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            detailSayur().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}