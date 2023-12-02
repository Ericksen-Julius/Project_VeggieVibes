package com.example.project.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.project.data.entity.Keranjang
import com.example.project.data.entity.Order
import com.example.project.data.entity.Sayur
import com.example.project.data.entity.User
import java.util.Date

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

    @Query("UPDATE user SET eMoney=:eMoney WHERE uid = :id")
    fun updateEmoney(eMoney: Int, id: Int)

    @Query("SELECT * FROM sayur WHERE pemilik IN (:pemilik)")
    fun loadSayurByIdPemilik(pemilik: Int?): List<Sayur>

    @Query("SELECT * FROM sayur WHERE pemilik NOT IN (:pemilik)")
    fun loadSayurSearch(pemilik: Int?): List<Sayur>

    @Insert
    fun insertAllSayur(vararg sayurs: Sayur)

    @Delete
    fun deleteSayur(sayur: Sayur)

    @Update
    fun updateSayur(sayur : Sayur)

    @Query("SELECT * FROM keranjang WHERE user_id IN (:pemilik)")
    fun loadKeranjangById(pemilik: Int?): List<Keranjang>

    @Query("SELECT * FROM keranjang WHERE user_id IN (:pemilik) AND sayur_id IN (:sayur)")
    fun checkKeranjang(pemilik: Int?, sayur: Int?): Keranjang?

    @Query("UPDATE keranjang SET COUNT=(:count) WHERE user_id IN (:pemilik) AND sayur_id IN (:sayur)")
    fun updateKeranjang(count:Int?,pemilik: Int?,sayur: Int)

    @Insert
    fun insertAllKeranjang(vararg keranjang: Keranjang)

    @Update
    fun updateKeranjang(keranjang: Keranjang)
    @Delete
    fun deleteKeranjang(keranjang: Keranjang)


    @Query("SELECT * FROM `order` WHERE uid_user IN (:pemilik)")
    fun loadOrder(pemilik: Int?): Order

    @Query("SELECT * FROM `order` WHERE uid_user IN (:pemilik) AND status = :status")
    fun loadListOrder(pemilik: Int?,status:String): List<Order>

    @Query("UPDATE `order` SET status = :status AND waktuSampai = :waktusampai WHERE uidorder = :uid")
    fun updateStatus(status:String,waktusampai: Date, uid:Int?)

    @Insert
    fun insertAllOrder(vararg order: Order)

}