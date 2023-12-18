package com.example.project.model.cost

import PABA.UAS.C14210151.model.cost.DcCost
import PABA.UAS.C14210151.model.cost.costModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface apiCost {
    @Headers("key: 9744b5322c3eee5704856dba7ba2fc6b")

    @POST("cost")
    fun getCost(
        @Body request: costModel
    ) : Call<DcCost>
}