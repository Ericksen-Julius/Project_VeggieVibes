package com.example.project.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProvinceResponse(
    @SerialName("rajaongkir")
    val rajaongkir: RajaOngkirProvinceResponse
)

@Serializable
data class RajaOngkirProvinceResponse(
    @SerialName("results")
    val results: List<Province>
)

@Serializable
data class Province(
    @SerialName("province_id")
    val province_id: String,
    @SerialName("province")
    val province: String
)


