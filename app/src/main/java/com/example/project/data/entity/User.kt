package com.example.project.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) var uid: Int? = null,
    @ColumnInfo(name = "full_name") var fullName : String?,
    @ColumnInfo(name = "email") var email : String?,
    @ColumnInfo(name = "password") var password : String?,
    @ColumnInfo(name = "phone") var phone : String?,
    @ColumnInfo(name = "asalKota") var asalKota : String?,
    @ColumnInfo(name = "alamat") var alamat : String?,
    @ColumnInfo(name = "namaToko") var namaToko : String?,
    @ColumnInfo(name = "eMoney") var eMoney : Int?
)
