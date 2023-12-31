package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.project.data.OnFragmentInteractionListener

class  Home : AppCompatActivity(), OnFragmentInteractionListener {
    private lateinit var btnCart: ImageView
    private lateinit var btnHome: ImageView
    private lateinit var btnSearch: ImageView
    private lateinit var btnShelf: ImageView
    private lateinit var btnAccount: ImageView
    private lateinit var btnOrders: ImageView
    private lateinit var judul: TextView

    private lateinit var mFragmentManager: FragmentManager


    override fun onFragmentColorChange(color: Int) {
        findViewById<ConstraintLayout>(R.id.wrapper).setBackgroundColor(color)
    }

    fun setTitle(judul : String){
        this.judul.text = judul
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        btnCart = findViewById(R.id.btnCart)
        btnHome = findViewById(R.id.btnHome)
        btnSearch = findViewById(R.id.btnSearch)
        btnShelf = findViewById(R.id.btnShelf)
        btnAccount = findViewById(R.id.btnAccount)
        btnOrders = findViewById(R.id.btnOrders)
        mFragmentManager = supportFragmentManager
        judul = findViewById(R.id.textView2)

        val uidUser = intent.getIntExtra("idUser",0)


        val mfHome = homeFragment()
        val bundle = Bundle()
        bundle.putInt("uidUser",uidUser)
        mfHome.arguments = bundle
        mFragmentManager.beginTransaction().add(R.id.frameContainer,mfHome,homeFragment::class.java.simpleName).commit()

        btnHome.setOnClickListener {
            val bundle1 = Bundle()
            bundle1.putInt("uidUser",uidUser)
            mfHome.arguments = bundle1
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainer,mfHome,homeFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
        btnShelf.setOnClickListener {
            val bundle1 = Bundle()
            bundle1.putInt("uidUser",uidUser)
            val mfRak = rakFragment()
            mfRak.arguments = bundle1
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainer,mfRak,rakFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
        btnAccount.setOnClickListener {
            val bundle1 = Bundle()
            bundle1.putInt("uidUser",uidUser)
            val mfAccount = detailAccount()
            mfAccount.arguments = bundle1
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainer,mfAccount,detailAccount::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
        btnOrders.setOnClickListener {
            val bundle1 = Bundle()
            bundle1.putInt("uidUser",uidUser)
            val mfAccount = Orders()
            mfAccount.arguments = bundle1
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainer,mfAccount,Orders::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
        btnSearch.setOnClickListener {
            val bundle1 = Bundle()
            bundle1.putInt("uidUser",uidUser)
            val mfSearch = searchFragment()
            mfSearch.arguments = bundle1
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainer,mfSearch,searchFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
        btnCart.setOnClickListener {
            val bundle1 = Bundle()
            bundle1.putInt("uidUser",uidUser)
            val mfCart = cartFragment()
            mfCart.arguments = bundle1
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainer,mfCart,cartFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }
}