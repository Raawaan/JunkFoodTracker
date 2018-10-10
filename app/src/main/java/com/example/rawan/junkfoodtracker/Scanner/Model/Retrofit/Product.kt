package com.example.rawan.junkfoodtracker

import com.google.gson.annotations.SerializedName

data class Product(
	@field:SerializedName("nutriments")
	val nutriments: Nutriments? = null,

	@field:SerializedName("brands_tags")
	val brandsTags: List<String?>? = null

)