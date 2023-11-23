package com.example.project

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
    private lateinit var edit: Button
    private lateinit var topUp: Button
    private lateinit var database: AppDatabase
    private lateinit var mFragmentManager: FragmentManager
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
        edit = view.findViewById(R.id.editAccount)
        topUp = view.findViewById(R.id.topUp)
        database = activity?.let { AppDatabase.getInstance(it.applicationContext) }!!
        val data = database.userDao().loadAllByIds(getIdUser)
        val formattedPrice = data?.eMoney?.let { formatDecimal(it) }
        nama.text = "Full Name: " + data.fullName
        emoney.text = "E-money: $formattedPrice"
        email.text = "Email: " + data.email
        noTelpon.text = "No telpon: " + data.phone
        mFragmentManager = parentFragmentManager
        topUp.setOnClickListener {
            showEditTextAlertDialog(requireContext(),getIdUser,data.fullName,data.email,data.password,data.phone)
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
    }
    fun formatDecimal(number: Int): String {
        val decimalFormat = DecimalFormat("#,##0")
        return decimalFormat.format(number)
    }
    private fun showEditTextAlertDialog(context: Context, idUser:Int?,nama:String?,email:String?,pass:String?,noTelpon:String?) {
//        val editText = EditText(context)
//        editText.hint = "100.000" // Set a hint for the EditText
//        editText.inputType = android.text.InputType.TYPE_CLASS_NUMBER
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
                // Called when tracking the seek bar starts
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Called when tracking the seek bar stops
            }
        })
//        totalText.setOnKeyListener { v, keyCode, event ->
//            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
//                val formattedText = totalText.text.toString()
//                var plainText : String
//                if (formattedText.toInt() < 1000){
//                    plainText = formattedText
//                }else{
//                    plainText = formattedText.replace(",", "")
//                }
//                val intValue = plainText.toIntOrNull()
//                if (intValue!!<10000){
//                    totalText.setText("10,000")
//                    Toast.makeText(requireContext(), "Top up minimal 10 ribu", Toast.LENGTH_SHORT).show()
//                }
//                seekBar.progress = 10_000
//                return@setOnKeyListener true
//            }
//            false
//        }
        totalText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Remove existing commas and get the numeric value
                val userInput = s.toString().replace(",", "")
                // Format the numeric value with commas using the US locale
                val formattedText = NumberFormat.getNumberInstance(Locale.US).format(userInput.toLong())

                // Remove the TextWatcher to prevent infinite loop
                totalText.removeTextChangedListener(this)

                // Convert the formatted text to a Long and clamp it to the specified range
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

                // Set the clamped value back to the EditText
                totalText.setText(NumberFormat.getNumberInstance(Locale.US).format(clampedValue))

                // Move the cursor to the end of the text
                totalText.setSelection(totalText.text.length)

                // Add the TextWatcher back
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
            database.userDao().updateUser(
                User(
                    idUser,
                    nama,
                    email,
                    pass,
                    noTelpon,
                    progress
                )
            )
            val data = database.userDao().loadAllByIds(idUser)
            this.emoney.text = "E-money: ${data.eMoney.toString()}"
            // For example, you can display a Toast with the entered text
            // Toast.makeText(context, "Entered Text: $enteredText", Toast.LENGTH_SHORT).show()
        }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
        }
        // Create and show the AlertDialog
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