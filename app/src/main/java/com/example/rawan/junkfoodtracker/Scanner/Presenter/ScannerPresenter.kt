package com.example.rawan.junkfoodtracker.Scanner.Presenter

import android.graphics.Bitmap
import java.util.*

/**
 * Created by rawan on 15/10/18.
 */
interface ScannerPresenter {
    fun onDetach()
    fun addProduct(productBarcode: Long, name: String, energy: Long,
                   saturatedFat: Long, sugars: Long, carbohydrates: Long,
                   date: Date, mail: String, userName: String,counter:Int)
    fun detectBarcode(photo: Bitmap)
    fun connection(barcode: Long)
}