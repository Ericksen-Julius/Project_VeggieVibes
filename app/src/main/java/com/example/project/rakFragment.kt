package com.example.project

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.adapter.sayurAdapter
import com.example.project.data.AppDatabase
import com.example.project.data.entity.Sayur
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [rakFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class rakFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var addButton : FloatingActionButton
    private lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterSayur: sayurAdapter
    private lateinit var mFragmentManager: FragmentManager
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
        return inflater.inflate(R.layout.fragment_rak, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (super.requireActivity() as Home).setTitle("My Vegetables")
        recyclerView = view.findViewById(R.id.recyclerView)
        adapterSayur = sayurAdapter(listSayur)
        database = activity?.let { AppDatabase.getInstance(it.applicationContext) }!!
        addButton = view.findViewById(R.id.floatingActionButton)
        val getIdUser = arguments?.getInt("uidUser")
        addButton.setOnClickListener {
            val mFragmentManager: FragmentManager = parentFragmentManager
            val bundle = Bundle()
            bundle.putInt("uidUser",getIdUser ?: 0)
            val mfAdd = addEditSayauranFragment()
            mfAdd.arguments = bundle
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainer,mfAdd,addEditSayauranFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
        mFragmentManager = parentFragmentManager
        val adapterP = adapterSayur
        recyclerView.adapter = adapterP
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapterP.setOnItemClickCallBack(object : sayurAdapter.onItemClickCallBack{
            override fun onItemClick(position: Int) {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle(listSayur[position].nama)
                dialog.setItems(R.array.items_option,DialogInterface.OnClickListener { dialog, which ->
                    if(which == 0){
                        val bundle = Bundle()
                        bundle.putInt("sayurId",listSayur[position].uidSayur?:0)
                        bundle.putInt("uidUser",getIdUser ?: 0)
                        val mfRak = addEditSayauranFragment()
                        mfRak.arguments = bundle
                        mFragmentManager.beginTransaction().apply {
                            replace(R.id.frameContainer,mfRak,rakFragment::class.java.simpleName)
                            addToBackStack(null)
                            commit()
                        }
                    }else if(which == 1){
                        getData(getIdUser?:0)
                        AlertDialog.Builder(requireContext())
                            .setTitle("Delete " + listSayur[position].nama)
                            .setPositiveButton(
                                "HAPUS",DialogInterface.OnClickListener{
                                        dialog, which ->
                                    database.userDao().deleteSayur(listSayur[position])
                                    listSayur.removeAt(position)
                                    adapterP.notifyDataSetChanged()
                                    dialog.dismiss()
                                    Toast.makeText(
                                        requireContext(),
                                        "Data Successfully Deleted",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            )
                            .setNegativeButton(
                                "BATAL",DialogInterface.OnClickListener { dialog, which ->
                                    dialog.dismiss()
                                }
                            ).show()
                    }else if(which == 2){
                        val bundle = Bundle()
                        bundle.putInt("sayurId",listSayur[position].uidSayur?:0)
                        val mfRak = detailSayur()
                        mfRak.arguments = bundle
                        mFragmentManager.beginTransaction().apply {
                            replace(R.id.frameContainer,mfRak,detailSayur::class.java.simpleName)
                            addToBackStack(null)
                            commit()
                        }
                    }else{
                        dialog.dismiss()
                    }
                })
                val dialogView = dialog.create()
                dialogView.show()
            }

        })
        getData(getIdUser?:0)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun getData(idPemilik : Int){
        listSayur.clear()
        listSayur.addAll(database.userDao().loadSayurByIdPemilik(idPemilik))
        adapterSayur.notifyDataSetChanged()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment rakFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            rakFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}