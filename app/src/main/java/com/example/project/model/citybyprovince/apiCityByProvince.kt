package com.example.project.model.citybyprovince

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface apiCityByProvince {
    @Headers("key: 68e90b83b10bd1d778665443c3e94889")

    @GET("city")
    fun getCity(
        @Query("province") id: String
    ) : Call<DcResponseCityByProvince>
}