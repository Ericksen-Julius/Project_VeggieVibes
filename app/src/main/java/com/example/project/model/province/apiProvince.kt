package com.example.project.model.province

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface apiProvince {
    @Headers("key: 9744b5322c3eee5704856dba7ba2fc6b")

    @GET("province")
    fun getProvince() : Call<DcResponseProvince>
}