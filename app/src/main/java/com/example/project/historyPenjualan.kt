package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.adapter.detailPenjualanAdapter
import com.example.project.data.AppDatabase
import com.example.project.data.entity.Penjualan

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [historyPenjualan.newInstance] factory method to
 * create an instance of this fragment.
 */
class historyPenjualan : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterhistoryPenjualan: detailPenjualanAdapter
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var database: AppDatabase
    private var listKeranjang : MutableList<Penjualan> = mutableListOf<Penjualan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvDetailPenjualan)
        adapterhistoryPenjualan = detailPenjualanAdapter(listKeranjang)
        database = activity?.let { AppDatabase.getInstance(it.applicationContext) }!!
        val getIdSayur = arguments?.getInt("uid_sayur")
        mFragmentManager = parentFragmentManager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapterhistoryPenjualan
        getData(getIdSayur!!)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_penjualan, container, false)
    }

    fun getData(idSayur: Int){
        listKeranjang.clear()
        listKeranjang.addAll(database.userDao().getPenjualan(idSayur!!))
        adapterhistoryPenjualan.notifyDataSetChanged()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment historyPenjualan.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            historyPenjualan().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}