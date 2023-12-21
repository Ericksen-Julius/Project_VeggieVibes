package com.example.project

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import com.example.project.data.AppDatabase
import com.example.project.data.entity.Keranjang
import com.example.project.data.entity.User
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [detailAccount.newInstance] factory method to
 * create an instance of this fragment.
 */
class detailAccount : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var nama: TextView
    private lateinit var emoney: TextView
    private lateinit var email: TextView
    private lateinit var noTelpon: TextView
    private lateinit var _asalKota: TextView
    private lateinit var _alamat: TextView
    private lateinit var passConf: EditText
    private lateinit var confirmation: EditText
    private lateinit var edit: Button
    private lateinit var topUp: Button
    private lateinit var delete: Button
    private lateinit var logout: Button
    private lateinit var database: AppDatabase
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var dialogView: View
//    private lateinit var getIdUser : Int

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
        return inflater.inflate(R.layout.fragment_detail_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (super.requireActivity() as Home).setTitle("Detail Account")
        val getIdUser = arguments?.getInt("uidUser",-1)
        nama = view.findViewById(R.id.fullname)
        emoney = view.findViewById(R.id.eMoney)
        email = view.findViewById(R.id.email)
        noTelpon = view.findViewById(R.id.phone)
        _asalKota = view.findViewById(R.id.asalKota)
        _alamat = view.findViewById(R.id.alamat)
        edit = view.findViewById(R.id.editAccount)
        topUp = view.findViewById(R.id.topUp)
        delete = view.findViewById(R.id.btnDel)
        logout = view.findViewById(R.id.logOut)
        database = activity?.let { AppDatabase.getInstance(it.applicationContext) }!!
        val data = database.userDao().loadAllByIds(getIdUser)
        val formattedPrice = data.eMoney?.let { formatDecimal(it) }
        nama.text = data.fullName
        emoney.text = "$formattedPrice"
        email.text = data.email
        noTelpon.text = data.phone
        _asalKota.text = data.asalKota
        _alamat.text = data.alamat
        mFragmentManager = parentFragmentManager
        topUp.setOnClickListener {
            showEditTextAlertDialog(requireContext(),getIdUser,data.fullName,data.email,data.password,data.phone, data.asalKota,data.alamat, data.namaToko)
        }
        edit.setOnClickListener {
            val bundle1 = Bundle()
            bundle1.putInt("uidUser",getIdUser?:0)
            val mfAccount = editAccount()
            mfAccount.arguments = bundle1
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainer,mfAccount,editAccount::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }

        delete.setOnClickListener {
            val inflater = LayoutInflater.from(context)
            dialogView = inflater.inflate(R.layout.fragment_delete_account, null)
            passConf = dialogView.findViewById(R.id.pass)
            confirmation = dialogView.findViewById(R.id.conf)

            val builder = AlertDialog.Builder(requireContext())
                .setTitle("Delete Account")
                .setView(dialogView)
                .setPositiveButton("Delete") { dialog, _ ->
                    if (!(passConf.text.toString().equals(data.password))) {
                        Toast.makeText(requireContext(), "Password Incorrect", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }

                    else if (!(confirmation.text.toString().equals("CONFIRM"))) {
                        Toast.makeText(requireContext(), "Type CONFIRM to Delete Account", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }

                    else {
                        database.userDao().delete(data)
                        dialog.dismiss()
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        requireActivity().finish()
                        startActivity(intent)
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
            val alertDialog = builder.create()
            alertDialog.show()
        }

        logout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
                .setTitle("Log Out")
                .setMessage("Are you sure you want to Log Out?")
                .setPositiveButton("Yes") { dialog, _ ->
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    requireActivity().finish()
                    startActivity(intent)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }


    fun formatDecimal(number: Int): String {
        val decimalFormat = DecimalFormat("#,##0")
        return decimalFormat.format(number)
    }
    private fun showEditTextAlertDialog(context: Context, idUser:Int?,nama:String?,email:String?,pass:String?,noTelpon:String?,asalKota:String?,alamat:String?,namaToko:String?) {
        val dialogView = layoutInflater.inflate(R.layout.seekbar_custom, null)
        val seekBar = dialogView.findViewById<SeekBar>(R.id.seekBar)
        seekBar.min = 10_000
        seekBar.max = 10_000_000
        seekBar.progress = 10_000
        val totalText : EditText = dialogView.findViewById(R.id.totalTopUp)
        totalText.setText("10,000")

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                totalText.setText("${formatDecimal(progress)}")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        totalText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val userInput = s.toString().replace(",", "")
                totalText.removeTextChangedListener(this)
                val intValue = userInput.toLongOrNull() ?: 0
                val clampedValue = when {
                    intValue < 10000 -> {
                        Toast.makeText(requireContext(), "Top Up Minimal 10 ribu", Toast.LENGTH_SHORT).show()
                        10000
                    }
                    intValue > 10000000 -> {
                        Toast.makeText(requireContext(), "Top Up Maximal 10 juta", Toast.LENGTH_SHORT).show()
                        10000000
                    }
                    else -> intValue
                }

                totalText.setText(NumberFormat.getNumberInstance(Locale.US).format(clampedValue))

                totalText.setSelection(totalText.text.length)

                totalText.addTextChangedListener(this)
                seekBar.progress = intValue.toInt()

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Top Up")
        alertDialogBuilder.setView(dialogView)

        // Set up the buttons
        alertDialogBuilder.setPositiveButton("OK") { dialog: DialogInterface, which: Int ->
//            val enteredText = editText.text.toString()
            val progress = seekBar.progress
            // Do something with the entered text
            var dataUser = database.userDao().loadAllByIds(idUser)
            database.userDao().updateUser(
                User(
                    idUser,
                    nama,
                    email,
                    pass,
                    noTelpon,
                    asalKota,
                    alamat,
                    namaToko,
                    dataUser.eMoney!! + progress
                )
            )
            dataUser = database.userDao().loadAllByIds(idUser)
            this.emoney.text = "Rp. ${dataUser.eMoney?.let { formatDecimal(it) }}"
        }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment detailAccount.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            detailAccount().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}