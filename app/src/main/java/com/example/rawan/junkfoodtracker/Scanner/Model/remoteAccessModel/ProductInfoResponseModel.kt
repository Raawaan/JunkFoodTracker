package com.example.rawan.junkfoodtracker.Scanner.Model.remoteAccessModel

import com.example.rawan.junkfoodtracker.Response
import com.example.rawan.junkfoodtracker.Room.ProductEntity
import com.example.rawan.junkfoodtracker.Scanner.Model.remoteAccessModel.Retrofit.API
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback

/**
 * Created by rawan on 16/10/18.
 */
class ProductInfoResponseModel {
    val api = API.create()
    fun connectToAPI(barcode: Long): Observable<ProductEntity> {
        return api.getData(barcode)
                .map { response ->
            val brandName = response.product?.brandsTags?.get(0)!!
            val energy = response.product.nutriments?.energy!!.toLong()
            val saturatedFat = response.product.nutriments.saturatedFat!!.toLong()
            val sugars = response.product.nutriments.sugars?.toLong() ?: 0
            val carbohydrates = response.product.nutriments.carbohydrates!!.toLong()
            ProductEntity(barcode, brandName, energy, saturatedFat, sugars, carbohydrates)
        }


    }
}