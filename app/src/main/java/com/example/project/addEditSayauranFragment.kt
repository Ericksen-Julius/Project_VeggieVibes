package com.example.project

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.project.data.AppDatabase
import com.example.project.data.OnFragmentInteractionListener
import com.example.project.data.entity.Sayur
import com.example.project.data.entity.User
import java.io.IOException
import java.lang.Exception
import java.lang.RuntimeException
import java.text.NumberFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [addEditSayauranFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class addEditSayauranFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var namaSayur: EditText
    private lateinit var hargaSayur: EditText
    private lateinit var beratSayur: EditText
    private lateinit var stokSayur: EditText
    private lateinit var uploadText: EditText
    private lateinit var saveBtn: Button
    private lateinit var preview: ImageView
    private lateinit var database: AppDatabase
    private lateinit var judul : TextView

    private var listener : OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener){
            listener = context
        }else{
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onResume() {
        super.onResume()
        listener?.onFragmentColorChange(Color.parseColor("#D3D3D3"))
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onPause() {
        super.onPause()
        // Notify the activity to reset the background color to white
        listener?.onFragmentColorChange(Color.WHITE)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit_sayauran, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        namaSayur = view.findViewById(R.id.namaSayur)
        hargaSayur = view.findViewById(R.id.hargaSayur)
        beratSayur = view.findViewById(R.id.beratSayur)
        stokSayur = view.findViewById(R.id.stokSayur)
        saveBtn = view.findViewById(R.id.saveSayur)
        preview = view.findViewById(R.id.imagePreview)
        judul = view.findViewById(R.id.judul)
        uploadText = view.findViewById(R.id.fotoSayur)
        database = AppDatabase.getInstance(requireContext())
        val getIdUser = arguments?.getInt("uidUser")
        val getIdSayur = arguments?.getInt("sayurId",-1)
//        Log.d("hey", getIdUser.toString())
        if(getIdSayur != -1){
            (super.requireActivity() as Home).setTitle("Edit sayuran")
            judul.text = "Edit Vegetable"
            val dataSayur = database.userDao().loadAllByIdsSayur(getIdSayur?:0)
            namaSayur.setText(dataSayur.nama)
            hargaSayur.setText(dataSayur.harga.toString())
            beratSayur.setText(dataSayur.berat.toString())
            stokSayur.setText(dataSayur.stok.toString())
            uploadText.setText(dataSayur.gambar)
            val bitmap: Bitmap? = handleKeyUpEvent(requireContext(),this.uploadText.text.toString())
            bitmap?.let {
                preview.setImageBitmap(bitmap)
            }
        }else{
            (super.requireActivity() as Home).setTitle("Add sayuran")
            judul.text = "Add New Vegetable"

        }
        hargaSayur.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val userInput = s.toString().replace(",", "")
                hargaSayur.removeTextChangedListener(this)
                val intValue = userInput.toLongOrNull() ?: 0
                val clampedValue = when {
                    intValue < 1 -> {
                        Toast.makeText(requireContext(), "Harga sayuran tidak boleh 0 rupiah!", Toast.LENGTH_SHORT).show()
                        1
                    }
                    else -> intValue
                }

                hargaSayur.setText(NumberFormat.getNumberInstance(Locale.US).format(clampedValue))

                hargaSayur.setSelection(hargaSayur.text.length)

                hargaSayur.addTextChangedListener(this)
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        beratSayur.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val userInput = s.toString().replace(",", "")
                beratSayur.removeTextChangedListener(this)
                val intValue = userInput.toLongOrNull() ?: 0
                val clampedValue = when {
                    intValue < 1 -> {
                        Toast.makeText(requireContext(), "Berat sayuran harus lebih dari 1 gram!", Toast.LENGTH_SHORT).show()
                        1
                    }
                    else -> intValue
                }

                beratSayur.setText(NumberFormat.getNumberInstance(Locale.US).format(clampedValue))
                beratSayur.setSelection(beratSayur.text.length)
                beratSayur.addTextChangedListener(this)
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        stokSayur.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val userInput = s.toString().replace(",", "")
                stokSayur.removeTextChangedListener(this)
                val intValue = userInput.toLongOrNull() ?: 0
                val clampedValue = when {
                    intValue < 0 -> {
                        Toast.makeText(requireContext(), "Stok tidak boleh minus!", Toast.LENGTH_SHORT).show()
                        1
                    }
                    else -> intValue
                }
                stokSayur.setText(NumberFormat.getNumberInstance(Locale.US).format(clampedValue))
                stokSayur.setSelection(stokSayur.text.length)
                stokSayur.addTextChangedListener(this)
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        uploadText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (isFileInAssets(requireContext(), this.uploadText.text.toString())) {
                    val bitmap: Bitmap? = handleKeyUpEvent(requireContext(),this.uploadText.text.toString())
                    bitmap?.let {
                        preview.setImageBitmap(bitmap)
                    }
                } else {
                    val bitmap: Bitmap? = handleKeyUpEvent(requireContext(),"notfound.jpg")
                    bitmap?.let {
                        preview.setImageBitmap(bitmap)
                    }
                }

                return@setOnKeyListener true
            }
            false
        }
        saveBtn.setOnClickListener {
            if(namaSayur.text.isNotEmpty() && hargaSayur.text.isNotEmpty() && beratSayur.text.isNotEmpty() && stokSayur.text.isNotEmpty()){
                val harga = hargaSayur.text.toString().replace(",", "")
                val berat = beratSayur.text.toString().replace(",", "")
                val jumlah = stokSayur.text.toString().replace(",", "")
                if(getIdSayur != -1){
                    val soldSayur = database.userDao().loadAllByIdsSayur(getIdSayur!!).sold
                    database.userDao().updateSayur(
                        Sayur(
                            getIdSayur,
                            namaSayur.text.toString(),
                            getIdUser,
                            berat.toInt(),
                            harga.toInt(),
                            soldSayur,
                            jumlah.toInt(),
                            uploadText.text.toString()
                        )
                    )
                }else{
                    database.userDao().insertAllSayur(
                        Sayur(
                            null,
                            namaSayur.text.toString(),
                            getIdUser,
                            berat.toInt(),
                            harga.toInt(),
                            0,
                            jumlah.toInt(),
                            uploadText.text.toString()
                        )
                    )
                }
                requireActivity().supportFragmentManager.popBackStack()
            }else{
                Toast.makeText(requireContext(),"Silahkan isi data dengan valid!!", Toast.LENGTH_SHORT).show()
            }
        }

//        uploadBtn.setOnClickListener {
//            this.pickImageGallery()
//            this.openImagePicker()
//        }

    }

    private fun handleKeyUpEvent(context: Context, filename: String): Bitmap? {
        return try {
            val inputStream = context.assets?.open(filename)
            BitmapFactory.decodeStream(inputStream)
        } catch (e:Exception){
            e.printStackTrace()
            null
        }
    }
    fun isFileInAssets(context: Context, fileName: String): Boolean {
        return try {
            val inputStream = context.assets.open(fileName)
            inputStream.close()
            true
        } catch (e: IOException) {
            false
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"  // Set MIME type to image/*
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    private fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png", "image/jpg"))
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Handle the selected image URI here
            val selectedImageUri = data.data
            preview.setImageURI(selectedImageUri)
            // Process the selected image URI as needed
        }
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // Handle the selected image URI here
            val selectedImageUri = data.data
            // Get the file name from the URI
            val fileName = selectedImageUri?.let { getFileName(it) }
            // Process the file name as needed
            if (fileName != null) {
                Log.d("hey",fileName)
            }
        }
    }
    private fun getFileName(uri: Uri): String {
        var result = ""
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                result = it.getString(displayNameIndex)
            }
        }
        return result
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment addEditSayauranFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            addEditSayauranFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        val IMAGE_REQUEST_CODE = 100
        val REQUEST_IMAGE_PICK = 101
    }
}