package com.example.project

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
 * Use the [searchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class searchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterSayur: searchAdapter
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var database: AppDatabase
    private lateinit var search: EditText
    private lateinit var dialogView: View
    private lateinit var decrementButton: Button
    private lateinit var incrementButton: Button
    private lateinit var textView: TextView
    private var count = 0
    private var listSayur : MutableList<Sayur> = mutableListOf<Sayur>()

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
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (super.requireActivity() as Home).setTitle("Market Place")
        recyclerView = view.findViewById(R.id.recycleViewItemSearch)
        adapterSayur = searchAdapter(listSayur)
        database = activity?.let { AppDatabase.getInstance(it.applicationContext) }!!
        val getIdUser = arguments?.getInt("uidUser")
        mFragmentManager = parentFragmentManager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapterSayur
        search = view.findViewById(R.id.searchEditText)
        val userId = getIdUser ?: 0
        getData(userId ?: return)


        search.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = search.text.toString().trim()
                adapterSayur.filter(query)
                return@setOnKeyListener true
            }
            false
        }
        addToCart(userId)

    }
//    private fun getYourDataList(): List<Sayur> {
//        // Return your list of data here
//    }
    @SuppressLint("NotifyDataSetChanged")
    fun getData(idPemilik: Int){
        listSayur.clear()
        listSayur.addAll(database.userDao().loadSayurSearch(idPemilik))
        adapterSayur.notifyDataSetChanged()
    }

    fun addToCart(userId: Int){
        adapterSayur.setOnItemClickCallBack(object : searchAdapter.OnItemClickCallBack {
            override fun onItemClick(position: Int) {
                val inflater = LayoutInflater.from(context)
                dialogView = inflater.inflate(R.layout.counter, null)
                decrementButton = dialogView.findViewById(R.id.decrementButton)
                incrementButton = dialogView.findViewById(R.id.incrementButton)
                textView = dialogView.findViewById(R.id.counterTextView)
                decrementButton.setOnClickListener {
                    decrementCount()
                }

                incrementButton.setOnClickListener {
                    incrementCount()
                }
                val keranjang = database.userDao().checkKeranjang(userId,listSayur[position].uidSayur)
                val builder = AlertDialog.Builder(requireContext())
                if (keranjang != null) {
                    textView.text = keranjang.count.toString()
                }
                builder.setView(dialogView)
                    .setTitle("Add to Cart")
                    .setPositiveButton("OK") { dialog, _ ->
                        if (keranjang != null){
                            database.userDao().updateKeranjang(
                                textView.text.toString().toInt(),
                                userId,
                                listSayur[position].uidSayur!!
                            )
                            Toast.makeText(requireContext(),"Berhasil mengupdate isi keranjang!!", Toast.LENGTH_SHORT).show()
                        }else{
                            database.userDao().insertAllKeranjang(
                                Keranjang(
                                    null,
                                    listSayur[position].uidSayur,
                                    userId,
                                    textView.text.toString().toInt()
                                )
                            )
                            Toast.makeText(requireContext(),"Berhasil memasukkan ke keranjang!!", Toast.LENGTH_SHORT).show()
                        }
                        dialog.dismiss()
                        addToCart(userId)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }

                val alertDialog = builder.create()
                alertDialog.show()
            }
        })
    }
    fun incrementCount() {
        textView.text = (textView.text.toString().toInt() + 1).toString()
    }

    fun decrementCount() {
        textView.text = (textView.text.toString().toInt() - 1).toString()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment searchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            searchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}