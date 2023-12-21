package com.example.project.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Penjualan (
    @PrimaryKey(autoGenerate = true) var uid_penjualan: Int? = null,
    @ColumnInfo(name = "sayur_id") var sayur_id : Int?,
    @ColumnInfo(name = "user_id") var user_id : Int?,
    @ColumnInfo(name = "count") var count : Int?
)