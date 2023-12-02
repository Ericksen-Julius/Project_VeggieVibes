package com.example.project.model.city

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface apiCity {
    @Headers("key: 68e90b83b10bd1d778665443c3e94889")

    @GET("city")
    fun getCityALl() : Call<DcCity>
}