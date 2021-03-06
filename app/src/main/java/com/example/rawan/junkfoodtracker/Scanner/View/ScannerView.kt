package com.example.rawan.junkfoodtracker.Scanner.View

import android.support.annotation.StringRes
import com.example.rawan.junkfoodtracker.Room.ProductEntity


/**
 * Created by rawan on 10/10/18.
 */
interface ScannerView {
    fun onFailed(msg:String)
    fun onSuccess( msg:String)
    fun updateViewResponse(productEntity: ProductEntity)
}