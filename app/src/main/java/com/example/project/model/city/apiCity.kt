package com.example.project.model.city

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface apiCity {
    @Headers("key: 9744b5322c3eee5704856dba7ba2fc6b")

    @GET("city")
    fun getCityALl() : Call<DcCity>
}