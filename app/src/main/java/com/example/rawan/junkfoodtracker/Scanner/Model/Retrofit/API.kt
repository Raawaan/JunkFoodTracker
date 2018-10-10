package com.example.rawan.junkfoodtracker.Scanner.Model.Retrofit

import com.example.rawan.junkfoodtracker.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by rawan on 16/09/18.
 */
interface API {
        @GET("api/v0/product/{barcode}.json")
        fun getData(@Path("barcode") barcode:Long):Call<Response>
        companion object {
            val BASE_URL= "https://world.openfoodfacts.org/"
            fun create(): API {
                val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                return retrofit.create(API::class.java)
            }
        }
}