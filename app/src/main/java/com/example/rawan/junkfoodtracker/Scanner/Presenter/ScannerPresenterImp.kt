package com.example.rawan.junkfoodtracker.Scanner.Presenter

import android.graphics.Bitmap
import com.example.rawan.junkfoodtracker.AsyncTaskJFT
import com.example.rawan.junkfoodtracker.R
import com.example.rawan.junkfoodtracker.Room.DateWithoutTime
import com.example.rawan.junkfoodtracker.Room.ProductEntity
import com.example.rawan.junkfoodtracker.Room.UserEntity
import com.example.rawan.junkfoodtracker.Scanner.Model.localAccessModel.localScannerModel
import com.example.rawan.junkfoodtracker.Scanner.Model.remoteAccessModel.ProductInfoResponseModel
import com.example.rawan.junkfoodtracker.Scanner.View.ScannerView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import java.util.*

/**
 * Created by rawan on 15/10/18.
 */
class ScannerPresenterImp(private val localScannerModel: localScannerModel,
                          private val productInfoResponseModel: ProductInfoResponseModel,
                          private val scannerView: ScannerView) : ScannerPresenter {
    override fun detectBarcode(photo: Bitmap) {
        val detector = FirebaseVision.getInstance().visionBarcodeDetector
        detector.detectInImage(FirebaseVisionImage.fromBitmap(photo))
                .addOnSuccessListener {
                    if (it.size == 0) {
                        scannerView.onFailed(R.string.error)
                    }
                    for (barcode in it) {
                        val valueType = barcode.valueType
                        scannerView.onSuccess(barcode.displayValue!!)
                        // See API reference for complete list of supported types
                        when (valueType) {
                            FirebaseVisionBarcode.TYPE_PRODUCT -> {
//                                if (InternetConnection.isOnline(this@ScannerActivity))
                                    connection(barcode.displayValue!!.toLong())
//                                else
//                                    Toast.makeText(this@ScannerActivity, getString(R.string.connection), Toast.LENGTH_SHORT).show()

                            }
                        }
                    }
                }
                .addOnFailureListener {
                        scannerView.onException(it.toString())
                }
    }
    override fun connection(barcode: Long) {
        productInfoResponseModel.connectToAPI(barcode,onSuccess = {
          scannerView.updateViewResponse(it)
      },onFail = {
          scannerView.onException(it)
      })
    }
    override fun addProduct(productBarcode: Long, name: String, energy: Long,
                            saturatedFat: Long, sugars: Long,
                            carbohydrates: Long, date: Date, mail: String, userName: String,
                            counter:Int) {
        AsyncTaskJFT(inBackground = {
        val product = ProductEntity(productBarcode, name, energy, saturatedFat, sugars, carbohydrates)
        if (localScannerModel.selectProductBarcodeIfExisted(productBarcode) == 0.toLong()) {
            localScannerModel.addProduct(product)
        }
        val userEntity = UserEntity(localScannerModel.selectUserWithEmail(mail),userName, mail)
        if (localScannerModel.selectUserWithEmail(mail) == 0) {
            localScannerModel.createUser(userEntity)
        }

        val sameUserProduct = localScannerModel.sameUserWithBarcodeAndDate(userEntity.id, product.barcode, DateWithoutTime.todayDateWithoutTime(date))
        if (sameUserProduct != 0) {
            localScannerModel.updateCounter(userEntity.id,product.barcode,counter)
        } else {
            localScannerModel.createNewUserProduct(userEntity.id,product.barcode,counter,DateWithoutTime.todayDateWithoutTime(date))
        }
        },onSuccess = {}).execute()
    }
}