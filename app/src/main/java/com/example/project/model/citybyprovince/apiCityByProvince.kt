package com.example.project.model.citybyprovince

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface apiCityByProvince {
    @Headers("key: 9744b5322c3eee5704856dba7ba2fc6b")

    @GET("city")
    fun getCity(
        @Query("province") id: String
    ) : Call<DcResponseCityByProvince>
}