package com.example.rawan.junkfoodtracker

import com.google.gson.annotations.SerializedName

data class Response(
	@field:SerializedName("product")
	val product: Product? = null,

	@field:SerializedName("status_verbose")
	val status: String? = null
)