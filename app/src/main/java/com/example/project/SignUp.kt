package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.project.data.AppDatabase
import com.example.project.data.entity.User

class SignUp : AppCompatActivity() {
    private lateinit var fullName: EditText
    private lateinit var password: EditText
    private lateinit var noTelpon: EditText
    private lateinit var email: EditText
    private lateinit var _asalKota: EditText
    private lateinit var _alamat: EditText
    private lateinit var btnSignUp: Button
    private lateinit var toSignIn: TextView
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        fullName = findViewById(R.id.fullname)
        email = findViewById(R.id.email)
        noTelpon = findViewById(R.id.noTelp)
        password = findViewById(R.id.password)
        _asalKota = findViewById(R.id.asalKota)
        _alamat = findViewById(R.id.alamat)

        btnSignUp = findViewById(R.id.btnSignUp)
        database = AppDatabase.getInstance(applicationContext)
        toSignIn = findViewById(R.id.toSignIp)

        toSignIn.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        btnSignUp.setOnClickListener {
            if(fullName.text.isNotEmpty() && email.text.isNotEmpty() && noTelpon.text.isNotEmpty() && password.text.isNotEmpty() && _asalKota.text.isNotEmpty() && _alamat.text.isNotEmpty()){
                val check = database.userDao().checkAccount(email.text.toString())
                if(check != null){
                    Toast.makeText(applicationContext,"Email sudah terdaftar",Toast.LENGTH_SHORT).show()
                }else {
                    database.userDao().insertAll(
                        User(
                            null,
                            fullName.text.toString(),
                            email.text.toString(),
                            password.text.toString(),
                            noTelpon.text.toString(),
                            _asalKota.text.toString(),
                            _alamat.text.toString(),
                            null,
                            0
                        )
                    )
                    Toast.makeText(applicationContext,"Berhasil membuat akun!!",Toast.LENGTH_SHORT).show()
                    finish()
                }

            }else{
                Toast.makeText(applicationContext,"Silahkan isi data dengan valid!!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}