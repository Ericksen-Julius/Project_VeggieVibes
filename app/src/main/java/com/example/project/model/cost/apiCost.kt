package com.example.project.model.cost

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface apiCost {
    @Headers("key: 68e90b83b10bd1d778665443c3e94889")

    @POST("cost")
    fun getCost(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("weight") weight: Int,
        @Query("courier") courier: String
    ) : Call<DcCost>
}