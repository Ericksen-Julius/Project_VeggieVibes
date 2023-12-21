package com.example.project

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import com.example.project.adapter.detailAdapter
import com.example.project.data.AppDatabase
import com.example.project.data.entity.Keranjang
import com.example.project.data.entity.Order
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailOrder.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailOrder : Fragment() {
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var adapterDetail : detailAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: AppDatabase
    private lateinit var grandtotal : TextView
    private lateinit var totalbelanja : TextView
    private lateinit var ongkir : TextView
    private var listOrders : MutableList<Order> = mutableListOf()
    private var listKeranjang : MutableList<Keranjang> = mutableListOf()
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_detail_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvDetailOrder)
        adapterDetail = detailAdapter(listOrders)
        database = activity?.let{AppDatabase.getInstance((it.applicationContext))}!!
        mFragmentManager = childFragmentManager
        val adapterP = adapterDetail
        val getIdUser = arguments?.getInt("uid_user")
        val getIdOrder = arguments?.getInt("uid_order")
        if (getIdOrder != null) {
            getData(idorder = getIdOrder)
        }
        recyclerView.adapter = adapterP
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        grandtotal = view.findViewById(R.id.grandtotal)
        totalbelanja = view.findViewById(R.id.total)
        ongkir = view.findViewById(R.id.ongkir)
        var total = 0
            for(i in 0..<listKeranjang.size){
            val id_sayur = listKeranjang[i].sayur_id
            val carisayur = id_sayur?.let { database.userDao().loadAllByIdsSayur(it)}
            val jumlah = listKeranjang[i].count
            val harga = carisayur?.harga
            val subtotal = jumlah?.times(harga!!)
            total += subtotal!!
        }
        val formattedtotal = "Rp. ${formatDecimal(total)}"
        totalbelanja.text = formattedtotal
        val grandtotal2 = listOrders[0].totalHarga
        val tmpongkir = grandtotal2?.minus(total)
        val formattedongkir = tmpongkir?.let { formatDecimal(it) }
        val formattedgrandtot = "Rp. ${grandtotal2?.let { formatDecimal(it) }}"
        ongkir.text = "Rp. $formattedongkir"
        grandtotal.text = formattedgrandtot

    }

    @SuppressLint("NotifyDataSetChanged")
    fun getData(idorder : Int){
        listOrders.clear()
        listOrders.add((database.userDao().loadOrderByIdOrder(idorder)))
        listKeranjang = listOrders[0].keranjang.toMutableList()
        Log.d(listKeranjang.size.toString(),"ukuran keranjang")
        adapterDetail.notifyDataSetChanged()
    }
    fun formatDecimal(number: Int): String {
        val decimalFormat = DecimalFormat("#,##0")
        return decimalFormat.format(number)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailOrder.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailOrder().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}