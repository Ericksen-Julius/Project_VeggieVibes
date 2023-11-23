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

class MainActivity : AppCompatActivity() {
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var signIn:Button
    private lateinit var toSignup: TextView
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signIn = findViewById(R.id.btnSignIn)
        toSignup = findViewById(R.id.toSignUp)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        database = AppDatabase.getInstance(applicationContext)
        signIn.setOnClickListener {
            val user = database.userDao().login(email.text.toString(),password.text.toString())
//            user.fullName?.let { it1 -> Log.d("check", it1) }
            if (user!=null){
                val intent = Intent(this,Home::class.java)
                intent.putExtra("idUser",user.uid)
                startActivity(intent)
            }else{
                Toast.makeText(applicationContext,"Data tidak valid!!", Toast.LENGTH_SHORT).show()
            }
        }
        toSignup.setOnClickListener {
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }
    }
}