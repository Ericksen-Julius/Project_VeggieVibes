package com.example.project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.project.data.AppDatabase
import com.example.project.data.entity.User
import com.example.project.model.citybyprovince.DcResponseCityByProvince
import com.example.project.model.citybyprovince.apiCityByProvince
import com.example.project.model.province.DcResponseProvince
import com.example.project.model.province.apiProvince
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    private lateinit var _alamat: EditText
    private lateinit var save: Button
    private lateinit var database: AppDatabase
    private lateinit var provincies: ArrayList<String>
    private lateinit var cities: ArrayList<String>
    private lateinit var provincies_id: ArrayList<String>
    private lateinit var cities_id: ArrayList<String>
    private lateinit var province: String
    private lateinit var province_id: String
    var city: String = ""
    private lateinit var adapterProvinsi: ArrayAdapter<String>
    private lateinit var adapterKota: ArrayAdapter<String>
    var city_id: String = "-1"
    private lateinit var dialogView : View
    private lateinit var alertDialog: androidx.appcompat.app.AlertDialog
    private lateinit var spinnerProvinsi : Spinner
    private lateinit var spinnerKota : Spinner
    var getIdUser : Int = 0

    object ApiProvince {
        const val baseURL: String = "https://api.rajaongkir.com/starter/"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create<apiProvince>(apiProvince::class.java)

    }

    object ApiCityByProvince {
        const val baseURL: String = "https://api.rajaongkir.com/starter/"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create<apiCityByProvince>(apiCityByProvince::class.java)
    }

    fun getDataCity(idProvince : String){
        showLoading()
        cities.clear()
        cities_id.clear()
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
                            x.cityName?.let { cities.add(it) }
                            x.cityId?.let { cities_id.add(it) }
                        }
                    }
                    adapterKota.addAll(cities)
                    adapterKota.notifyDataSetChanged()
                }
                unshowLoading()
            }

            override fun onFailure(call: Call<DcResponseCityByProvince>, t: Throwable) {
                Log.d("gagal","hoyyy")
            }

        })
    }

    fun getDataProvince(){
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
                            x.province?.let { provincies.add(it) }
                            x.provinceId?.let { provincies_id.add(it) }
                        }
                    }
                    adapterProvinsi= ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, provincies)
                    spinnerProvinsi.adapter = adapterProvinsi
//                    getDataCity("1")
                }
            }

            override fun onFailure(call: Call<DcResponseProvince>, t: Throwable) {
                Log.d("gagal","hoyyy")
            }

        })
    }

    fun unshowLoading(){
        alertDialog.dismiss()
    }
    fun showLoading(){
        val inflater = LayoutInflater.from(requireContext())
        dialogView = inflater.inflate(R.layout.loading_bar, null)
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        alertDialog = builder.create()
        alertDialog.show()
    }
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
        _alamat = view.findViewById(R.id.alamat)
        save = view.findViewById(R.id.save)
        database = activity?.let { AppDatabase.getInstance(it.applicationContext) }!!
        val dataUser = database.userDao().loadAllByIds(getIdUser)
        nama.setText(dataUser.fullName)
        password.setText(dataUser.password)
        email.setText(dataUser.email)
        noTelpon.setText(dataUser.phone)
        _alamat.setText(dataUser.alamat)

        spinnerProvinsi = view.findViewById(R.id.province)
        spinnerKota = view.findViewById(R.id.city)
        provincies = arrayListOf()
        provincies_id = arrayListOf()
        cities = arrayListOf()
        cities_id = arrayListOf()
        adapterKota = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, cities)
        spinnerKota.adapter = adapterKota

        getDataProvince()

        spinnerProvinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val select = provincies_id[position]
                province_id = select
                getDataCity(province_id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("cek","uhuy")
            }

        }

        spinnerKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val select = cities[position]
                city = select
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("cek2", "mdakmdk")
            }
        }

        save.setOnClickListener {
            database.userDao().updateUser(
                User(
                    getIdUser,
                    nama.text.toString(),
                    email.text.toString(),
                    password.text.toString(),
                    noTelpon.text.toString(),
                    city,
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