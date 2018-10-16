package com.example.rawan.junkfoodtracker

import com.google.gson.annotations.SerializedName

data class Nutriments(

		@field:SerializedName("saturated-fat")
		val saturatedFat: Double? = null,
		@field:SerializedName("energy")
		val energy: String? = null,

		@field:SerializedName("sugars")
		val sugars: Double? = null,

		@field:SerializedName("carbohydrates")
		val carbohydrates: Double? = null
)