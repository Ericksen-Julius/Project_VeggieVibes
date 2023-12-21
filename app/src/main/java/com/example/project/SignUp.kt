package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.example.project.data.AppDatabase
import com.example.project.data.entity.User
import com.example.project.model.city.DcCity
import com.example.project.model.city.apiCity
import com.example.project.model.citybyprovince.DcResponseCityByProvince
import com.example.project.model.citybyprovince.apiCityByProvince
import com.example.project.model.province.DcResponseProvince
import com.example.project.model.province.apiProvince
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUp : AppCompatActivity() {
    private lateinit var fullName: EditText
    private lateinit var password: EditText
    private lateinit var noTelpon: EditText
    private lateinit var email: EditText
    private lateinit var _alamat: EditText
    private lateinit var btnSignUp: Button
    private lateinit var toSignIn: TextView
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
                    adapterProvinsi= ArrayAdapter(this@SignUp, android.R.layout.simple_spinner_dropdown_item, provincies)
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
        val inflater = LayoutInflater.from(this)
        dialogView = inflater.inflate(R.layout.loading_bar, null)
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setView(dialogView)
        alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        fullName = findViewById(R.id.fullname)
        email = findViewById(R.id.email)
        noTelpon = findViewById(R.id.noTelp)
        password = findViewById(R.id.password)
        _alamat = findViewById(R.id.alamat)

        btnSignUp = findViewById(R.id.btnSignUp)
        database = AppDatabase.getInstance(applicationContext)
        toSignIn = findViewById(R.id.toSignIp)


        spinnerProvinsi = findViewById(R.id.province)
        spinnerKota = findViewById(R.id.city)
        provincies = arrayListOf()
        provincies_id = arrayListOf()
        cities = arrayListOf()
        cities_id = arrayListOf()
        adapterKota = ArrayAdapter(this@SignUp, android.R.layout.simple_spinner_dropdown_item, cities)
        spinnerKota.adapter = adapterKota

        toSignIn.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        getDataProvince()

        spinnerProvinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val select = provincies_id[position]
                province = provincies[position]
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

        btnSignUp.setOnClickListener {
            if(fullName.text.isNotEmpty() && email.text.isNotEmpty() && noTelpon.text.isNotEmpty() && password.text.isNotEmpty() && city != "" && _alamat.text.isNotEmpty()){
                val check = database.userDao().checkAccount(email.text.toString())
                if(check != null){
                    Toast.makeText(applicationContext,"Email sudah terdaftar",Toast.LENGTH_SHORT).show()
                }else {
                    val builder = AlertDialog.Builder(this)
                        .setTitle("Sign Up")
                        .setMessage("Apakah data anda sudah benar?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            database.userDao().insertAll(
                                User(
                                    null,
                                    fullName.text.toString(),
                                    email.text.toString(),
                                    password.text.toString(),
                                    noTelpon.text.toString(),
                                    city,
                                    _alamat.text.toString(),
                                    null,
                                    0
                                )
                            )
                            Toast.makeText(applicationContext,"Berhasil membuat akun!!",Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .setNegativeButton("Cancel") {dialog, _ ->
                            dialog.dismiss()
                        }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
            }else{
                Toast.makeText(applicationContext,"Silahkan isi data dengan valid!!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}