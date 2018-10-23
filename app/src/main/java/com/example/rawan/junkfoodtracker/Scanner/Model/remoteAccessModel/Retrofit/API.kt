package com.example.rawan.junkfoodtracker.Scanner.Model.remoteAccessModel.Retrofit

import com.example.rawan.junkfoodtracker.Response
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by rawan on 16/09/18.
 */
interface API {
        @GET("api/v0/product/{barcode}.json")
        fun getData(@Path("barcode") barcode:Long):Observable<Response>

        companion object {
            val BASE_URL= "https://world.openfoodfacts.org/"
            fun create(): API {
                val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()

                return retrofit.create(API::class.java)
            }
        }
}