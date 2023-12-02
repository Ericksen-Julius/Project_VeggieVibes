package com.example.project.model.province

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface apiProvince {
    @Headers("key: 68e90b83b10bd1d778665443c3e94889")

    @GET("province")
    fun getProvince() : Call<DcResponseProvince>
}