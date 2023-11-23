package com.example.project.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.project.data.entity.Sayur
import com.example.project.data.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: Int?): User

    @Query("SELECT * FROM user WHERE email IN (:email) AND password IN (:password)")
    fun login(email:String,password: String): User

    @Query("SELECT * FROM user WHERE email in (:email)")
    fun checkAccount(email: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)

    @Update
    fun updateUser(user: User)

    @Query("SELECT * FROM sayur")
    fun getAllSayur(): List<Sayur>

    @Query("SELECT COUNT (*) FROM sayur")
    fun getRowCountSayur(): Int

    @Query("SELECT * FROM sayur WHERE uidSayur IN (:sayurIds)")
    fun loadAllByIdsSayur(sayurIds: Int): Sayur

    @Query("SELECT * FROM sayur WHERE pemilik IN (:pemilik)")
    fun loadSayurByIdPemilik(pemilik: Int?): List<Sayur>

    @Insert
    fun insertAllSayur(vararg sayurs: Sayur)

    @Delete
    fun deleteSayur(sayur: Sayur)

    @Update
    fun updateSayur(sayur : Sayur)
}