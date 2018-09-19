package com.example.rawan.junkfoodtracker

import android.content.Context
import android.widget.Toast
import com.example.rawan.junkfoodtracker.Retrofit.API
import kotlinx.android.synthetic.main.scanner.*
import retrofit2.Call
import retrofit2.Callback

/**
 * Created by rawan on 18/09/18.
 */
class RetrofitCall{
    val scannerActivity=ScannerActivity()
    companion object {
        fun newInstance():RetrofitCall {
            return RetrofitCall()
        }
    }
    fun makeCall(barcode:Long,context: Context){

    }
}