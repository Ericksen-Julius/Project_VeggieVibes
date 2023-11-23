package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.project.data.AppDatabase
import com.example.project.data.entity.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [editAccount.newInstance] factory method to
 * create an instance of this fragment.
 */
class editAccount : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var nama: EditText
    private lateinit var password: EditText
    private lateinit var email: EditText
    private lateinit var noTelpon: EditText
    private lateinit var _asalKota: EditText
    private lateinit var _alamat: EditText
    private lateinit var save: Button
    private lateinit var database: AppDatabase

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
        return inflater.inflate(R.layout.fragment_edit_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (super.requireActivity() as Home).setTitle("Edit Account")
        val getIdUser = arguments?.getInt("uidUser",-1)
        nama = view.findViewById(R.id.fullname)
        password = view.findViewById(R.id.password)
        email = view.findViewById(R.id.email)
        noTelpon = view.findViewById(R.id.noTelp)
        _asalKota = view.findViewById(R.id.asalKota)
        _alamat = view.findViewById(R.id.alamat)
        save = view.findViewById(R.id.save)
        database = activity?.let { AppDatabase.getInstance(it.applicationContext) }!!
        val dataUser = database.userDao().loadAllByIds(getIdUser)
        nama.setText(dataUser.fullName)
        password.setText(dataUser.password)
        email.setText(dataUser.email)
        noTelpon.setText(dataUser.phone)
        save.setOnClickListener {
            database.userDao().updateUser(
                User(
                    getIdUser,
                    nama.text.toString(),
                    email.text.toString(),
                    password.text.toString(),
                    noTelpon.text.toString(),
                    _asalKota.text.toString(),
                    _alamat.text.toString(),
                    dataUser.namaToko,
                    dataUser.eMoney
                )
            )
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment editAccount.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            editAccount().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}