package com.example.rawan.junkfoodtracker.Scanner.Model.remoteAccessModel

import com.example.rawan.junkfoodtracker.Response
import com.example.rawan.junkfoodtracker.Room.ProductEntity
import com.example.rawan.junkfoodtracker.Scanner.Model.remoteAccessModel.Retrofit.API
import retrofit2.Call
import retrofit2.Callback

/**
 * Created by rawan on 16/10/18.
 */
class ProductInfoResponseModel{
    val api = API.create()
    fun connectToAPI(barcode: Long, onSuccess:(ProductEntity)->Unit, onFail:(String)->Unit){
        val call: Call<Response> = api.getData(barcode)
        call.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>?, response: retrofit2.Response<Response>?) {
                if (!response?.body()?.status.equals("product found")) {
                    onFail(response?.body()?.status!!)
//                    Toast.makeText(this@ScannerActivity, "${response?.body()?.status}", Toast.LENGTH_SHORT).show()
                } else {
                    val brandName = response?.body()?.product?.brandsTags?.get(0)!!
                    val energy = response.body()?.product?.nutriments?.energy!!.toLong()
                    val saturatedFat = response.body()?.product?.nutriments?.saturatedFat!!.toLong()
                    val sugars = response.body()?.product?.nutriments?.sugars!!.toLong()
                    val carbohydrates = response.body()?.product?.nutriments?.carbohydrates!!.toLong()
                    val productEntityL= ProductEntity(barcode,brandName,energy, saturatedFat, sugars, carbohydrates)
                    onSuccess(productEntityL)
                }
            }
            override fun onFailure(call: Call<Response>?, t: Throwable?) {
                onFail(t.toString())
//                Toast.makeText(this@ScannerActivity, "$t", Toast.LENGTH_SHORT).show()
            }
        })
    }
}