package com.example.project

import PABA.UAS.C14210151.model.cost.DcCost
import PABA.UAS.C14210151.model.cost.costModel
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
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.project.data.AppDatabase
import com.example.project.data.entity.Keranjang
import com.example.project.data.entity.Order
import com.example.project.data.entity.Penjualan
import com.example.project.data.entity.Sayur
import com.example.project.data.entity.User
import com.example.project.model.city.DcCity
import com.example.project.model.city.apiCity
import com.example.project.model.citybyprovince.DcResponseCityByProvince
import com.example.project.model.citybyprovince.apiCityByProvince
import com.example.project.model.cost.apiCost
import com.example.project.model.province.DcResponseProvince
import com.example.project.model.province.apiProvince
import kotlinx.coroutines.DelicateCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
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
    private lateinit var _cost: TextView
    private lateinit var _harga: TextView
    private lateinit var _kotaAsal: TextView
    private lateinit var _kotaTujuan: TextView
    private lateinit var _kurir: TextView
    private lateinit var root: ConstraintLayout
    private lateinit var dialogView : View
    private lateinit var alertDialog: androidx.appcompat.app.AlertDialog
    private lateinit var loading: ProgressBar
    private lateinit var provinsi: ArrayList<String>
    private lateinit var provinsiId: ArrayList<String>
    private lateinit var kotaId: ArrayList<String>
    private lateinit var kota: ArrayList<String>
    private lateinit var kurir: ArrayList<String>
    private lateinit var costArray: ArrayList<Int>
    private lateinit var cityAsalId: Array<String>
    private lateinit var adapterProvinsi: ArrayAdapter<String>
    private lateinit var adapterKota: ArrayAdapter<String>
    private var idChoosen: String = "-1"
    private var courier: String = "jne"
    var subTotal = 0

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
        return inflater.inflate(R.layout.fragment_check_out_page, container, false)
    }

    object ApiProvince {
        const val baseURL: String = "https://api.rajaongkir.com/starter/"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create<apiProvince>(apiProvince::class.java)

    }

    object ApiCity {
        const val baseURL: String = "https://api.rajaongkir.com/starter/"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create<apiCity>(apiCity::class.java)

    }


    object ApiCityByProvince {
        const val baseURL: String = "https://api.rajaongkir.com/starter/"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create<apiCityByProvince>(apiCityByProvince::class.java)

    }

    object ApiCost {
        const val baseURL: String = "https://api.rajaongkir.com/starter/"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create<apiCost>(apiCost::class.java)

    }

    fun getCost(origin: String, destination: String, weight: Int, kurir: String){
        showLoading()
        var callApi = ApiCost.apiService.getCost(costModel( origin,destination,weight,kurir))
        callApi.enqueue(object : Callback<DcCost> {
            override fun onResponse(
                call: Call<DcCost>,
                response: Response<DcCost>
            ) {
                if (response.isSuccessful){
                    val hasil_cost = response.body()
                    for (x in hasil_cost!!.rajaongkir!!.results?.get(0)!!.costs!!){
                        if (x != null) {
                            x.cost!![0]!!.value?.let { costArray.add(it) }
                            if (costArray.size == cityAsalId.size){
                                Log.d("origin",origin)
                                Log.d("x.cost", x.cost!![0]!!.value.toString())
                                Log.d("city asal id",cityAsalId.toString())
                                _cost.text = "Rp.${formatDecimal(costArray.sum())}"
                                Log.d("cek ongkir cost",costArray.sum().toString())
                                _harga.text = "Rp.${formatDecimal(subTotal + costArray.sum())}"
                                unshowLoading()
//                                return
                            }
                            return
                        }
//                        if (origin == cityAsalId[cityAsalId.size-1]){
//                            _cost.text = "Rp.${formatDecimal(costArray.sum())}"
//                            _harga.text = "Rp.${formatDecimal(subTotal + costArray.sum())}"
//                            unshowLoading()
//                            return
//                        }
                    }
                }
            }
            override fun onFailure(call: Call<DcCost>, t: Throwable) {
                Log.d("gagal","hoyyy")
            }

        })
    }

    fun getDataCity(idProvince : String){
        showLoading()
        kota.clear()
        kotaId.clear()
        idChoosen = "-1"
        adapterKota.clear()
        var callApi = ApiCityByProvince.apiService.getCity(idProvince)
        callApi.enqueue(object : Callback<DcResponseCityByProvince> {
            override fun onResponse(
                call: Call<DcResponseCityByProvince>,
                response: Response<DcResponseCityByProvince>
            ) {
                if (response.isSuccessful){
                    val hasil_Provinsi = response.body()
                    for (x in hasil_Provinsi!!.rajaongkir!!.results!!){
                        if (x != null) {
                            x.cityName?.let { kota.add(it) }
                            x.cityId?.let { kotaId.add(it) }
                        }
                    }
                    adapterKota.addAll(kota)
                    adapterKota.notifyDataSetChanged()
                    unshowLoading()
                }
            }

            override fun onFailure(call: Call<DcResponseCityByProvince>, t: Throwable) {
                Log.d("gagal","hoyyy")
            }

        })
    }


    fun getDataProvince(){
        showLoading()
        var callApi = ApiProvince.apiService.getProvince()
        callApi.enqueue(object : Callback<DcResponseProvince> {
            override fun onResponse(
                call: Call<DcResponseProvince>,
                response: Response<DcResponseProvince>
            ) {
                if (response.isSuccessful){
                    val hasil_Provinsi = response.body()
                    for (x in hasil_Provinsi!!.rajaongkir!!.results!!){
                        if (x != null) {
                            x.province?.let { provinsi.add(it) }
                            x.provinceId?.let { provinsiId.add(it) }
                        }
                    }
                    adapterProvinsi= ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, provinsi)
                    spinnerProvinsi.adapter = adapterProvinsi
                    unshowLoading()
                    getDataCity("1")
                }
            }

            override fun onFailure(call: Call<DcResponseProvince>, t: Throwable) {
                Log.d("gagal","hoyyy")
            }

        })
    }
    fun getCity(arrayCity: ArrayList<String>){
        var callApi = ApiCity.apiService.getCityALl()
        callApi.enqueue(object : Callback<DcCity> {
            override fun onResponse(
                call: Call<DcCity>,
                response: Response<DcCity>
            ) {
                if (response.isSuccessful){
                    val hasil_city = response.body()
                    for (x in hasil_city!!.rajaongkir!!.results!!){
                        if (!cityAsalId.contains("")){
                            Log.d("Kota ditemukan 2", cityAsalId.toString())
                            return
                        }
                        if (x != null) {
                            if(arrayCity.contains(x.cityName)){
                                x.cityName?.let { Log.d("Kota ditemukan", it) }
                                val tmp2 = arrayCity.indexOf(x.cityName)
                                Log.d("Index ditemukan", tmp2.toString())
                                cityAsalId[tmp2] = x.cityId.toString()
                            }
                        }else{
                            Log.d("gagal2","hoyyy")
                        }
                    }
                }
            }
            override fun onFailure(call: Call<DcCity>, t: Throwable) {
                Log.d("gagal","hoyyy")
            }
        })
    }

    fun unshowLoading(){
        Log.d("cuyy","dismiss")
        alertDialog.dismiss()
    }
    fun showLoading(){
        alertDialog.show()
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (super.requireActivity() as Home).setTitle("Checkout Page")
        val getIdUser = arguments?.getInt("uidUser")
        database = activity?.let { AppDatabase.getInstance(it.applicationContext) }!!
        val dataKeranjang = database.userDao().loadKeranjangById(getIdUser)
        var alamat : EditText = view.findViewById(R.id.alamat)
        alamat.setText(database.userDao().loadAllByIds(getIdUser).alamat)
        var kotaAsal = ArrayList<String>()
        var totalWeight = ArrayList<Int>()
        var totalPrice = ArrayList<Int>()
        var sayurIdKeranjang = ArrayList<Int>()
        var totalSold = ArrayList<Int>()

        var weightTotal = 0
        for (x in dataKeranjang){
            val dataSayur = database.userDao().loadAllByIdsSayur(x.sayur_id!!)
            val dataPemilik = database.userDao().loadAllByIds(dataSayur.pemilik)
            sayurIdKeranjang.add(x.sayur_id!!)
            totalSold.add(x.count!!)
            Log.d("total sold",totalSold.toString())
            if (!kotaAsal.contains(dataPemilik.asalKota)){
                dataPemilik.asalKota?.let { kotaAsal.add(it) }
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
        kotaText = if (kotaAsal.size == 1){
            kotaAsal[0]
        }else{
            "${kotaAsal.size} kota asal"
        }
        cityAsalId = Array(kotaAsal.size){""}
        getCity(kotaAsal)
        Log.d("check city asal",cityAsalId.toString())
        root = view.findViewById(R.id.rootCheckOut)
        spinnerProvinsi = view.findViewById(R.id.provinsi)
        spinnerKota = view.findViewById(R.id.kota)
        spinnerKurir = view.findViewById(R.id.kurir)
        _berat = view.findViewById(R.id.berat)
        _harga = view.findViewById(R.id.totalHarga)
        _kotaAsal = view.findViewById(R.id.origin)
        _kotaTujuan = view.findViewById(R.id.destination)
        _kurir = view.findViewById(R.id.kurirText)
        _cost = view.findViewById(R.id.cost)
        database = activity?.let { AppDatabase.getInstance(it.applicationContext) }!!
        buttonCheckOut = view.findViewById(R.id.checkout)
        provinsi = arrayListOf()
        provinsiId = arrayListOf()
        kota = arrayListOf()
        kotaId = arrayListOf()
        costArray = arrayListOf()
        kurir = arrayListOf("JNE","POST","TIKI")
        adapterKota= ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, kota)
        spinnerKota.adapter = adapterKota
        val inflater = LayoutInflater.from(context)
        dialogView = inflater.inflate(R.layout.loading_bar, null)
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        alertDialog = builder.create()

        getDataProvince()

        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            kurir
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerKurir.adapter = adapter
        }


        _kotaAsal.text = kotaText
        _harga.text = "Rp.${formatDecimal(subTotal)}"
        _berat.text = "${formatDecimal(weightTotal)} Gram"
        Log.d("Array kota",kotaAsal.toString())
        spinnerProvinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                getDataCity(provinsiId[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        var loadAwal = true
        spinnerKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                idChoosen = kotaId[position]
                _kotaTujuan.text = kota[position]
                costArray.clear()
//                showLoading()
                for (x in kotaAsal.indices){
                    Log.d("chekk",cityAsalId[x])
                    Log.d("chekk2", idChoosen)
                    Log.d("chekk3", totalWeight[x].toString())
                    Log.d("chekk4", _kurir.text.toString())
                    getCost(cityAsalId[x],idChoosen,totalWeight[x],_kurir.text.toString().toLowerCase())
                }
                loadAwal = false
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
                courier = kurir[position]
                if(!loadAwal){
                    costArray.clear()

                    for(x in kotaAsal.indices){
                        getCost(cityAsalId[x],idChoosen,totalWeight[x],_kurir.text.toString().toLowerCase())
                    }
                }


            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
        buttonCheckOut.setOnClickListener {
            checkOnClick(getIdUser,kotaAsal,totalPrice,totalWeight,costArray,dataKeranjang,sayurIdKeranjang,totalSold,alamat.text.toString())
        }
    }

    fun checkOnClick(
        idUser:Int?,
        array1: ArrayList<String>,
        array2:ArrayList<Int>,
        array3: ArrayList<Int>,
        arrayCost: ArrayList<Int>,
        dataKeranjang: List<Keranjang>,
        idSayur: ArrayList<Int>,
        totalSold: ArrayList<Int>,
        alamat : String
    ){
        Log.d("cek ongkir",arrayCost.sum().toString())
        val alertDialogBuilder = AlertDialog.Builder(context)
        var textMessage = ""
        for (x in array1.indices){
            textMessage += "${x+1}.\n" +
                    "Asal kota: ${array1[x]}\n"+
                    "Harga: ${formatDecimal(array2[x])}\n"+
                    "Berat: ${formatDecimal(array3[x])} Gram\n"+
                    "Cost: ${formatDecimal(arrayCost[x])}\n"
        }
        alertDialogBuilder.setTitle("Checkout Dialog")
        alertDialogBuilder.setMessage(textMessage)
        val currentDate = Date()
        alertDialogBuilder.setPositiveButton("Checkout") { dialog: DialogInterface, which: Int ->
//            val enteredText = editText.text.toString()
            if(database.userDao().loadAllByIds(idUser).eMoney!! >= array2.sum() + arrayCost.sum()){
                database.userDao().insertAllOrder(
                    Order(
                        null,
                        dataKeranjang,
                        idUser,
                        "Shipping",
                        array2.sum() + arrayCost.sum(),
                        currentDate,
                        null,
                        alamat
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
                        dataSayur.stok?.minus(totalSold[x]),
                        dataSayur.gambar
                        )
                    )
                    val dataPemilik = database.userDao().loadAllByIds(dataSayur.pemilik)
                    database.userDao().updateEmoney(dataPemilik.eMoney!!.plus(array2.sum()), dataSayur.pemilik!!)
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
                        dataUser.eMoney?.minus(array2.sum() + arrayCost.sum())
                    )
                )
                val idKeranjang = database.userDao().loadKeranjangById(idUser)
                for(x in idKeranjang){
                    database.userDao().insertPenjualan(
                        Penjualan(
                            null,
                            x.sayur_id,
                            x.user_id,
                            x.count
                        )
                    )
                }
                for (x in idKeranjang){
                    database.userDao().deleteKeranjang(x)
                }
                Toast.makeText(requireContext(), "Berhasil Checkout!!", Toast.LENGTH_SHORT).show()
                val bundle1 = Bundle()
                val mfHome = homeFragment()
                bundle1.putInt("uidUser",idUser!!)
                mfHome.arguments = bundle1
                mFragmentManager = parentFragmentManager
                mFragmentManager.beginTransaction().apply {
                    replace(R.id.frameContainer,mfHome,homeFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
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