package com.example.project.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

@Entity
data class Order (
    @PrimaryKey(autoGenerate = true) var uidorder: Int? = null,
    @ColumnInfo(name = "listKeranjang") var keranjang : List<Keranjang>,
    @ColumnInfo(name = "uid_user") var uid_user : Int?,
    @ColumnInfo(name = "status") var status : String?,
    @ColumnInfo(name = "totalHarga") var totalHarga : Int?,
    @ColumnInfo(name = "waktu") var waktu : Date
)

//object DateConverter {
//    @TypeConverter
//    @JvmStatic
//    fun toDate(timestamp: Long?): Date? {
//        return timestamp?.let { Date(it) }
//    }
//
//    @TypeConverter
//    @JvmStatic
//    fun toTimestamp(date: Date?): Long? {
//        return date?.time
//    }
//}