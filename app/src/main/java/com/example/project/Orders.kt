package com.example.project

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Orders.newInstance] factory method to
 * create an instance of this fragment.
 */
class Orders : Fragment() {
    private lateinit var btnHistory : TextView
    private lateinit var btnOngoing : TextView
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var frameContainer2 : FrameLayout
    private lateinit var judul : TextView
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnHistory = view.findViewById(R.id.btnHistory)
        btnOngoing = view.findViewById(R.id.btnOngoing)
        mFragmentManager = childFragmentManager
        var getIdUser = arguments?.getInt("uidUser")


        btnHistory.setOnClickListener {
//            underLine(btnHistory)
//            cancelUnderLine(btnOngoing)
            val bundle1 = Bundle()
            if (getIdUser != null) {
                bundle1.putInt("uidUser",getIdUser)
            }
            val mfAccount = History()
            mfAccount.arguments = bundle1
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainerOrders,mfAccount,History::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }

        btnOngoing.setOnClickListener {
//            underLine(btnOngoing)
//            cancelUnderLine(btnHistory)
            val bundle1 = Bundle()
            if (getIdUser != null) {
                bundle1.putInt("uidUser",getIdUser)
            }
            val mfAccount = Ongoing()
            mfAccount.arguments = bundle1
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainerOrders,mfAccount,Ongoing::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

    fun underLine(text : TextView){
        val mString = text.toString()
        val mSpannableString = SpannableString(mString)
        mSpannableString.setSpan(UnderlineSpan(),0,mSpannableString.length,0)
        text.text = mSpannableString
    }
    fun cancelUnderLine(text : TextView){
        val mString = text.toString()
        val normalSpannableString = SpannableString(mString)
        text.text =normalSpannableString
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_orders, container, false)

        // Replace "someViewId" with the actual ID of the view in your parent layout
        val container = view.findViewById<FrameLayout>(R.id.frameContainerOrders)


        // Add the ChildFragment to the container
        val childFragment = History()
        childFragmentManager.beginTransaction()
            .replace(R.id.frameContainerOrders, childFragment)
            .commit()

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment History.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Orders().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}