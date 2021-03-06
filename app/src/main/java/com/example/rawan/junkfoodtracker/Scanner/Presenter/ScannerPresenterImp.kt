package com.example.rawan.junkfoodtracker.Scanner.Presenter

import android.graphics.Bitmap
import com.example.rawan.junkfoodtracker.AsyncTaskJFT
import com.example.rawan.junkfoodtracker.Room.DateWithoutTime
import com.example.rawan.junkfoodtracker.Room.ProductEntity
import com.example.rawan.junkfoodtracker.Room.UserEntity
import com.example.rawan.junkfoodtracker.Scanner.Model.localAccessModel.LocalScannerModel
import com.example.rawan.junkfoodtracker.Scanner.Model.remoteAccessModel.ProductInfoResponseModel
import com.example.rawan.junkfoodtracker.Scanner.View.ScannerView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by rawan on 15/10/18.
 */
class ScannerPresenterImp(private val localScannerModel: LocalScannerModel,
                          private val productInfoResponseModel: ProductInfoResponseModel,
                          private val scannerView: ScannerView) : ScannerPresenter {
    private val compositeDisposable = CompositeDisposable()
    override fun onDetach() {
        compositeDisposable.clear()
    }

    override fun detectBarcode(photo: Bitmap) {
        val detector = FirebaseVision.getInstance().visionBarcodeDetector
        detector.detectInImage(FirebaseVisionImage.fromBitmap(photo))
                .addOnSuccessListener {
                    if (it.size == 0) {
                        scannerView.onFailed("Please try again")
                    }
                    for (barcode in it) {
                        val valueType = barcode.valueType
                        scannerView.onSuccess(barcode.displayValue!!)
                        // See API reference for complete list of supported types
                        when (valueType) {
                            FirebaseVisionBarcode.TYPE_PRODUCT -> {
                                connection(barcode.displayValue!!.toLong())
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    scannerView.onFailed(it.toString())
                }
    }
    override fun connection(barcode: Long) {
        compositeDisposable.add(productInfoResponseModel.connectToAPI(barcode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                        onNext = {
                            scannerView.updateViewResponse(it)
                        },
                        onError = {
                            scannerView.onFailed(it.localizedMessage)
                        })
        )
    }
    override fun addProduct(productBarcode: Long, name: String, energy: Long,
                            saturatedFat: Long, sugars: Long,
                            carbohydrates: Long, date: Date, mail: String, userName: String,
                            counter: Int) {
        val product = ProductEntity(productBarcode, name, energy, saturatedFat,
                sugars, carbohydrates)
        var userEntity = UserEntity(0, userName, mail)

        localScannerModel.selectProductBarcodeIfExisted(productBarcode)
                .zipWith(localScannerModel.selectUserWithEmail(mail))
                .flatMap {
                    if (it.first == (0).toLong()) {
                        localScannerModel.addProduct(product)
                    }
                    userEntity = UserEntity(it.second, userName, mail)
                    if (it.second == 0) {
                        userEntity.id = localScannerModel.createUser(userEntity).toInt()
                    }

                    return@flatMap localScannerModel.sameUserWithBarcodeAndDate(userEntity.id,
                            product.barcode, DateWithoutTime.todayDateWithoutTime(date))
                }.map {
                    if (it != 0) {
                        localScannerModel.updateCounter(userEntity.id, product.barcode, counter)
                    } else {
                        localScannerModel.createNewUserProduct(userEntity.id, product.barcode, counter,
                                DateWithoutTime.todayDateWithoutTime(date))
                    }
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = {
                            scannerView.onFailed(it.toString())
                        }, onNext = {
                    scannerView.onSuccess("3ASH")
                })
    }
}
//        localScannerModel.selectProductBarcodeIfExisted(productBarcode).flatMap {
//            val product = ProductEntity(productBarcode, name, energy, saturatedFat,
//                    sugars, carbohydrates)
//            if (it  == 0.toLong()) {
//                localScannerModel.addProduct(product)
//            }
//            return@flatMap localScannerModel.selectUserWithEmail(mail).flatMap {
//                val userEntity = UserEntity(it, userName, mail)
//                if (it  == 0) {
//                    userEntity.id = localScannerModel.createUser(userEntity).toInt()
//                }
//                return@flatMap localScannerModel.sameUserWithBarcodeAndDate(userEntity.id,
//                        product.barcode, DateWithoutTime.todayDateWithoutTime(date)).map {
//                    if (it != 0) {
//                        localScannerModel.updateCounter(userEntity.id, product.barcode, counter)
//                    } else {
//                        localScannerModel.createNewUserProduct(userEntity.id, product.barcode, counter,
//                                DateWithoutTime.todayDateWithoutTime(date))
//                    }
//                }
//            }
//        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeBy(onError = {
//            scannerView.onFailed("L2AAAAAAAA")
//        },onNext = {
//            scannerView.onSuccess("3ASH")
//        })

//
//    val product = ProductEntity(productBarcode, name, energy, saturatedFat,
//            sugars, carbohydrates)
//    if (localScannerModel.selectProductBarcodeIfExisted(productBarcode) == 0.toLong()) {
//        localScannerModel.addProduct(product)
//    }
//
//
//    val userEntity = UserEntity(localScannerModel.selectUserWithEmail(mail), userName, mail)
//    if (localScannerModel.selectUserWithEmail(mail) == 0) {
//        userEntity.id = localScannerModel.createUser(userEntity).toInt()
//    }
//
//
//    val sameUserProduct = localScannerModel.sameUserWithBarcodeAndDate(userEntity.id,
//            product.barcode, DateWithoutTime.todayDateWithoutTime(date))
//    if (sameUserProduct != 0) {
//        localScannerModel.updateCounter(userEntity.id, product.barcode, counter)
//    } else {
//        localScannerModel.createNewUserProduct(userEntity.id, product.barcode, counter,
//                DateWithoutTime.todayDateWithoutTime(date))
//    }