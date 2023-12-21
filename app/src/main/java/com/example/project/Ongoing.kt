package com.example.project

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.adapter.orderAdapter
import com.example.project.adapter.sayurAdapter
import com.example.project.data.AppDatabase
import com.example.project.data.entity.Order
import com.example.project.data.entity.Sayur
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Ongoing.newInstance] factory method to
 * create an instance of this fragment.
 */
class Ongoing : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var btnDone : Button
    private lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterOrder: orderAdapter
    private lateinit var mFragmentManager: FragmentManager
    private var listOrders : MutableList<Order> = mutableListOf<Order>()

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
        return inflater.inflate(R.layout.fragment_ongoing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvOngoing)
        adapterOrder = orderAdapter(listOrders)
        database = activity?.let { AppDatabase.getInstance(it.applicationContext) }!!
        mFragmentManager = parentFragmentManager
        val adapterP = adapterOrder
        val getIdUser = arguments?.getInt("uidUser")
        recyclerView.adapter = adapterP
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        getData(getIdUser?:0)
        adapterP.setOnItemClickCallBack(object : orderAdapter.onItemClickCallBack{
            override fun onItemDone(order: Order) {
                val alertDialogBuilder = AlertDialog.Builder(context)
                alertDialogBuilder.setTitle("Konfirmasi")
                alertDialogBuilder.setMessage("Apakah anda yakin ingin menyelesaikan transaksi ini ?")
                alertDialogBuilder.setPositiveButton("Ya") { dialog, which ->
                    val currentDate = Date()
//                database.userDao().updateStatus("Done",currentDate,listOrders[position].uidorder)
                    database.userDao().updateStatusOrder(
                        Order(
                            order.uidorder,
                            order.keranjang,
                            order.uid_user,
                            "Done",
                            order.totalHarga,
                            order.waktuDatang,
                            currentDate
                        )
                    )
//                Log.d("database update status",position.toString())
                    getData(getIdUser!!)
                }
                alertDialogBuilder.setNegativeButton("Batal"){dialog,which->
                    dialog.dismiss()
                }
                val dialog = alertDialogBuilder.create()
                dialog.show()

            }

            override fun toDetail(position: Int) {
                val bundle1 = Bundle()
                if (getIdUser != null) {
                    listOrders[position].uid_user?.let { bundle1.putInt("uid_user", it) }
                    listOrders[position].uidorder?.let { bundle1.putInt("uid_order", it) }
                }
                val mfAccount = DetailOrder()
                mfAccount.arguments = bundle1
                mFragmentManager.beginTransaction().apply {
                    replace(R.id.frameContainerOrders,mfAccount,DetailOrder::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
        })

    }

    @SuppressLint("NotifyDataSetChanged")
    fun getData(idPemilik : Int){
        listOrders.clear()
        listOrders.addAll((database.userDao().loadListOrder(idPemilik,"Shipping")))
        adapterOrder.notifyDataSetChanged()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Ongoing.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Ongoing().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}