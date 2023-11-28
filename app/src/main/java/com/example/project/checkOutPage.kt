package com.example.project

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.project.data.AppDatabase
import com.example.project.data.entity.Keranjang
import com.example.project.data.entity.Order
import com.example.project.data.entity.Sayur
import com.example.project.data.entity.User
import com.example.project.model.Province
import com.example.project.model.ProvinceResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.DelicateCoroutinesApi
import java.text.DecimalFormat
import java.time.LocalDate
import java.util.Date


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [checkOutPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class checkOutPage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var spinnerProvinsi : Spinner
    private lateinit var spinnerKota : Spinner
    private lateinit var spinnerKurir : Spinner
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var buttonCheckOut: Button
    private lateinit var database: AppDatabase
    private lateinit var _berat: TextView
    private lateinit var _harga: TextView
    private lateinit var _kotaAsal: TextView
    private lateinit var _kotaTujuan: TextView
    private lateinit var _kurir: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private suspend fun getProvinceList(apiKey: String): List<Province> {

        val client = HttpClient()

        val url = "https://api.rajaongkir.com/starter/province"
//
        Log.d("dedede","")
        val response = client.get<ProvinceResponse>(url){
                header("key", apiKey)
                contentType(ContentType.Application.Json)
            }
        client.close()
        Log.d("dididi",response.rajaongkir.results[0].province)

        return response.rajaongkir.results
//        var prov = ArrayList<Province>()
//        prov.add(Province("1","2"))
//        return prov
//        Log.d("cui", response.toString())
//        client.close()
//        return response.rajaongkir.results



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_out_page, container, false)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (super.requireActivity() as Home).setTitle("Checkout Page")
//        val provinces = runBlocking {
//            getProvinceList("76f567ee91df772c42426d5af9987622")
//        }
//        provinces.forEach {
//            Log.d("hey","${it.provinceId}: ${it.provinceName}")
//        }
//        GlobalScope.launch(Dispatchers.Main) {
//            val result = getProvinceList("f58c67748981e1cdd34832549247109e")
//            for (x in result){
//                Log.d(x.provinceId,x.provinceName)
//            }
//        }
        val provinsi = arrayListOf<String>("Jawa Timur","Jawa Tengah","Jawa Barat","Jakarta")
        val kota = arrayListOf<String>("Surabaya","Jakarta Pusat","Malang","Mojokerto")
        val kurir = arrayListOf<String>("JNE","TIKI","POST")

        spinnerProvinsi = view.findViewById(R.id.provinsi)
        spinnerKota = view.findViewById(R.id.kota)
        spinnerKurir = view.findViewById(R.id.kurir)
        _berat = view.findViewById(R.id.berat)
        _harga = view.findViewById(R.id.totalHarga)
        _kotaAsal = view.findViewById(R.id.origin)
        _kotaTujuan = view.findViewById(R.id.destination)
        _kurir = view.findViewById(R.id.kurirText)
        database = activity?.let { AppDatabase.getInstance(it.applicationContext) }!!
        buttonCheckOut = view.findViewById(R.id.checkout)


        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            provinsi
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerProvinsi.adapter = adapter
        }
        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            kota
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerKota.adapter = adapter
        }
        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            kurir
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerKurir.adapter = adapter
        }
        val getIdUser = arguments?.getInt("uidUser")
        database = activity?.let { AppDatabase.getInstance(it.applicationContext) }!!
        val dataKeranjang = database.userDao().loadKeranjangById(getIdUser)
        var kotaTujuan = ArrayList<String>()
        var date = Date()
        var totalWeight = ArrayList<Int>()
        var totalPrice = ArrayList<Int>()
        var sayurIdKeranjang = ArrayList<Int>()
        var totalSold = ArrayList<Int>()
        var subTotal = 0
        var weightTotal = 0
        for (x in dataKeranjang){
            val dataSayur = database.userDao().loadAllByIdsSayur(x.sayur_id!!)
            val dataPemilik = database.userDao().loadAllByIds(dataSayur.pemilik)
            sayurIdKeranjang.add(x.sayur_id!!)
            totalSold.add(x.count!!)
            if (!kotaTujuan.contains(dataPemilik.asalKota)){
                dataPemilik.asalKota?.let { kotaTujuan.add(it) }
                totalWeight.add(x.count?.let { dataSayur.berat?.times(it) } ?: 0)
                totalPrice.add(x.count?.let { dataSayur.harga?.times(it) } ?: 0)
            }else{
                var tmp1 =  totalWeight[totalWeight.lastIndex]
                var tmp2 =  totalPrice[totalPrice.lastIndex]
                tmp1 += x.count?.let { dataSayur.berat?.times(it) } ?: 0
                tmp2 += x.count?.let { dataSayur.harga?.times(it) } ?: 0
                totalWeight[totalWeight.lastIndex] = tmp1
                totalPrice[totalPrice.lastIndex] = tmp2
            }
            subTotal += x.count?.let { dataSayur.harga?.times(it) } ?: 0
            weightTotal += x.count?.let { dataSayur.berat?.times(it) } ?: 0
        }
        var kotaText = ""
        kotaText = if (kotaTujuan.size == 1){
            kotaTujuan[0]
        }else{
            "${kotaTujuan.size} kota asal"
        }
        _kotaAsal.text = date.toString()
        _harga.text = "Rp.${formatDecimal(subTotal)}"
        _berat.text = "${formatDecimal(weightTotal)} Gram"
        spinnerKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                _kotaTujuan.text = kota[position]
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
        spinnerKurir.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                _kurir.text = kurir[position]
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
        buttonCheckOut.setOnClickListener {
            checkOnClick(getIdUser,kotaTujuan,totalPrice,totalWeight,dataKeranjang,sayurIdKeranjang,totalSold)
        }

    }

    fun checkOnClick(
        idUser:Int?,
        array1: ArrayList<String>,
        array2:ArrayList<Int>, array3: ArrayList<Int>,
        dataKeranjang: List<Keranjang>,
        idSayur: ArrayList<Int>,
        totalSold: ArrayList<Int>
    ){
        val alertDialogBuilder = AlertDialog.Builder(context)
        var textMessage = ""
        for (x in array1.indices){
            textMessage += "${x+1}.\n" +
                    "Asal kota: ${array1[x]}\n"+
                    "Harga: Rp.${formatDecimal(array2[x])}\n"+
                    "Berat: ${formatDecimal(array3[x])} Gram\n"
        }
        alertDialogBuilder.setTitle("Checkout Dialog")
        alertDialogBuilder.setMessage(textMessage)
        val currentDate = Date()
        alertDialogBuilder.setPositiveButton("Checkout") { dialog: DialogInterface, which: Int ->
//            val enteredText = editText.text.toString()
            if(database.userDao().loadAllByIds(idUser).eMoney!! >= array2.sum()){
                database.userDao().insertAllOrder(
                    Order(
                        null,
                        dataKeranjang,
                        idUser,
                        "Shipping",
                        array2.sum(),
                        currentDate,
                        null
                    )
                )
                for (x in idSayur.indices){
                    val dataSayur = database.userDao().loadAllByIdsSayur(idSayur[x])
                    database.userDao().updateSayur(
                        Sayur(
                        idSayur[x],
                        dataSayur.nama,
                        dataSayur.pemilik,
                        dataSayur.berat,
                        dataSayur.harga,
                            dataSayur.sold?.plus(totalSold[x]),
                        totalSold[x],
                        dataSayur.gambar
                        )
                    )
                }
                val dataUser = database.userDao().loadAllByIds(idUser)
                database.userDao().updateUser(
                    User(
                        dataUser.uid,
                        dataUser.fullName,
                        dataUser.email,
                        dataUser.password,
                        dataUser.phone,
                        dataUser.asalKota,
                        dataUser.alamat,
                        dataUser.namaToko,
                        dataUser.eMoney?.minus(array2.sum())
                    )
                )
                val idKeranjang = database.userDao().loadKeranjangById(idUser)
                for (x in idKeranjang){
                    database.userDao().deleteKeranjang(x)
                }
                Toast.makeText(requireContext(), "Berhasil Checkout!!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "E-money tidak cukup, silahkan top up terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun formatDecimal(number: Int): String {
        val decimalFormat = DecimalFormat("#,##0")
        return decimalFormat.format(number)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment checkOutPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            checkOutPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}