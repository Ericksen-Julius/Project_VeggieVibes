package com.example.project

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.adapter.cartAdapter
import com.example.project.adapter.searchAdapter
import com.example.project.data.AppDatabase
import com.example.project.data.entity.Keranjang
import com.example.project.data.entity.Sayur

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [cartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class cartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterKeranjang: cartAdapter
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var database: AppDatabase
    private var listKeranjang : MutableList<Keranjang> = mutableListOf<Keranjang>()


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
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewCart)
        adapterKeranjang = cartAdapter(listKeranjang)
        database = activity?.let { AppDatabase.getInstance(it.applicationContext) }!!
        val getIdUser = arguments?.getInt("uidUser")
        mFragmentManager = parentFragmentManager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapterKeranjang
        getData(getIdUser!!)
        adapterKeranjang.setOnItemClickCallBack(object : cartAdapter.OnItemClickCallBack{
            override fun deleteCart(position: Int) {
                val builder = AlertDialog.Builder(requireContext())
                    .setTitle("Delete item")
                    .setMessage("Are you sure?")
                    .setPositiveButton("OK") { dialog, _ ->
                        database.userDao().deleteKeranjang(listKeranjang[position])
                        Toast.makeText(requireContext(),"Berhasil menghapus item", Toast.LENGTH_SHORT).show()
                        getData(getIdUser)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }

                val alertDialog = builder.create()
                alertDialog.show()
            }
        })

    }
    @SuppressLint("NotifyDataSetChanged")
    fun getData(idPemilik: Int){
        listKeranjang.clear()
        listKeranjang.addAll(database.userDao().loadKeranjangById(idPemilik))
        adapterKeranjang.notifyDataSetChanged()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment cartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            cartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}