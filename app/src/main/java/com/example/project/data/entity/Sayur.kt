package com.example.project.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Sayur(
    @PrimaryKey(autoGenerate = true) var uidSayur: Int? = null,
    @ColumnInfo(name = "nama") var nama : String?,
    @ColumnInfo(name = "pemilik") var pemilik : Int?,
    @ColumnInfo(name = "berat") var berat : Int?,
    @ColumnInfo(name = "harga") var harga : Int?,
    @ColumnInfo(name = "sold") var sold : Int?,
    @ColumnInfo(name = "stok") var stok : Int?,
    @ColumnInfo(name = "gambar") var gambar : String?
)
