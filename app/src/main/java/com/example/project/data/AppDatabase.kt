package com.example.project.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.project.converter.DateConverter
import com.example.project.converter.KeranjangListConverter
import com.example.project.data.dao.UserDao
import com.example.project.data.entity.Keranjang
import com.example.project.data.entity.Order
import com.example.project.data.entity.Penjualan
import com.example.project.data.entity.Sayur
import com.example.project.data.entity.User

@Database(entities = [User::class,Sayur::class,Keranjang::class,Order::class,Penjualan::class], version = 10)
@TypeConverters(DateConverter::class,KeranjangListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object{
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            if (instance == null){
                instance = Room.databaseBuilder(context,AppDatabase::class.java,"app-database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }

            return instance!!
        }
    }
}