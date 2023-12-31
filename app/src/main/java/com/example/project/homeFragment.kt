package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.widget.ViewPager2
import com.example.project.adapter.carouselAdapter
import com.example.project.data.AppDatabase
import com.example.project.data.entity.Keranjang
import com.example.project.data.entity.Sayur

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [homeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class homeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var database: AppDatabase
    private lateinit var nameTag : TextView
    private lateinit var carousel: ViewPager2
    private lateinit var adapterCarousel: carouselAdapter
    private lateinit var dialogView: View
    private lateinit var decrementButton: Button
    private lateinit var incrementButton: Button
    private lateinit var textView: TextView
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (super.requireActivity() as Home).setTitle("VeggieVibes")
        var getIdUser = arguments?.getInt("uidUser")
        carousel = view.findViewById(R.id.carousel)
        adapterCarousel = carouselAdapter(listSayur)
        database = activity?.let { AppDatabase.getInstance(it.applicationContext) }!!
        carousel.adapter = adapterCarousel
        getData()
        nameTag = view.findViewById(R.id.nameUser)
        val user = database.userDao().loadAllByIds(getIdUser)
        val text = "Welcome Back\n${user.fullName}"
        val buyProducts = view.findViewById<Button>(R.id.btnBuyProducts)
        nameTag.text = text
        buyProducts.setOnClickListener {
            val mfBuyProducts = searchFragment()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainer, mfBuyProducts, searchFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }
    fun getData(){
        listSayur.clear()
        database.userDao().getTop3SoldSayurByPemilik()?.let { listSayur.addAll(it) }
        adapterCarousel.notifyDataSetChanged()
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
         * @return A new instance of fragment homeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            homeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}