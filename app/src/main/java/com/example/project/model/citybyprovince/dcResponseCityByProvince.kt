package com.example.project.model.citybyprovince

import com.google.gson.annotations.SerializedName

data class DcResponseCityByProvince(

	@field:SerializedName("rajaongkir")
	val rajaongkir: Rajaongkir? = null
)

data class Query(

	@field:SerializedName("province")
	val province: String? = null
)

data class ResultsItem(

	@field:SerializedName("city_name")
	val cityName: String? = null,

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("province_id")
	val provinceId: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("postal_code")
	val postalCode: String? = null,

	@field:SerializedName("city_id")
	val cityId: String? = null
)

data class Rajaongkir(

	@field:SerializedName("query")
	val query: Query? = null,

	@field:SerializedName("results")
	val results: List<ResultsItem?>? = null,

	@field:SerializedName("status")
	val status: Status? = null
)

data class Status(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("description")
	val description: String? = null
)
