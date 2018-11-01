package com.example.rawan.junkfoodtracker.Scanner.Model.localAccessModel

import com.example.rawan.junkfoodtracker.Room.*
import io.reactivex.Observable

/**
 * Created by rawan on 10/10/18.
 */
class LocalScannerModel(private val database: JFTDatabase) {
    fun sameUserWithBarcodeAndDate(userId: Int, barcode: Long, selectedDate: Long): Observable<Int> {
         return Observable.fromCallable {
            if (database.upDao().sameUserSameProduct(userId,
                            barcode, selectedDate)==0)
                0
            else
                database.upDao().sameUserSameProduct(userId,
                        barcode, selectedDate)
        }
    }
    fun updateCounter(userId:Int,barcode:Long,counterPM:Int){
       var ToBeUpdated= database.upDao().loadToUpdateCounter(userId, barcode)
        ToBeUpdated+=counterPM
        database.upDao().update(ToBeUpdated, userId, barcode)
    }
    fun createNewUserProduct(userId: Int, barcode: Long, counterPM: Int, selectedDate: Long) {
        val userProductEntry = UserProductEntity(userId, barcode,
                counterPM, selectedDate)
        database.upDao().insertUp(userProductEntry)
    }
    fun selectProductBarcodeIfExisted(barcode: Long): Observable<Long> {
        return Observable.fromCallable {
            if (database.productDao().selectProductWithBarcode(barcode)==0.toLong())
                0
            else
                database.productDao().selectProductWithBarcode(barcode)
           }
    }
    fun addProduct(product: ProductEntity) {
        database.productDao().insertProduct(product)
    }
    fun selectUserWithEmail(email: String): Observable<Int>{
        return Observable.fromCallable {
            if (database.userDao().selectUserWithEmail(email)==0){
                0
            }
            else
                database.userDao().selectUserWithEmail(email)
           }
    }
    fun createUser(userEntity: UserEntity): Long {
        return database.userDao().insertUser(userEntity)
    }
}