package com.example.rawan.junkfoodtracker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.scanner.*
import com.example.android.todolist.AppExecutors
import com.example.rawan.junkfoodtracker.Retrofit.API
import com.example.rawan.roomjft.Room.JFTDatabase
import com.example.rawan.roomjft.Room.ProductEntity
import com.example.rawan.roomjft.Room.UserEntity
import com.example.rawan.roomjft.Room.UserProductEntity
import com.facebook.stetho.Stetho
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import java.util.*


/**
 * Created by rawan on 10/09/1b.
 */
class Scanner:AppCompatActivity(){
    val api = API.create()
    val fbAuth= FirebaseAuth.getInstance()
    lateinit var photo:Bitmap
    companion object {
        val CHOOSE_IMAGE_REQUEST=10
    }

//    override fun onSaveInstanceState(outState: Bundle?) {
//        super.onSaveInstanceState(outState)
//        iv_barcode.setImageBitmap(photo)
//    }
    lateinit var  image:FirebaseVisionImage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner)
        setSupportActionBar(Scannertoolbar)
        Stetho.initializeWithDefaults(this)
        supportActionBar?.title = "Scanner"
        choose_img.setOnClickListener {
            chooseImage()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==CHOOSE_IMAGE_REQUEST&&resultCode== Activity.RESULT_OK){
            photo= data!!.extras.get("data") as Bitmap
            barcodeDetection(photo)
        }
    }
    fun chooseImage(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CHOOSE_IMAGE_REQUEST)
    }
    fun barcodeDetection(photo:Bitmap){
        image = FirebaseVisionImage.fromBitmap(photo)
        iv_barcode.setImageBitmap(photo)
        val detector = FirebaseVision.getInstance().visionBarcodeDetector
        detector.detectInImage(image)
                .addOnSuccessListener {
                    if (it.size==0){
                        Toast.makeText(this@Scanner, "Please try again", Toast.LENGTH_SHORT).show()
                    }
                    for (barcode in it) {
                        val valueType = barcode.getValueType()
                        Toast.makeText(this@Scanner, "${barcode.displayValue}", Toast.LENGTH_SHORT).show()
                        // See API reference for complete list of supported types
                        when (valueType) {
                            FirebaseVisionBarcode.TYPE_PRODUCT->{
                                Toast.makeText(this@Scanner, "Type: Product", Toast.LENGTH_SHORT).show()
                                connection(barcode.displayValue!!.toLong())
                            }
                            FirebaseVisionBarcode.TYPE_WIFI -> {
                                val ssid = barcode.wifi?.getSsid()
                                val type = barcode.wifi?.getEncryptionType()
                                Toast.makeText(this@Scanner, "Type: Wi-Fi ${ssid}  $type", Toast.LENGTH_SHORT).show()
                            }
                            FirebaseVisionBarcode.TYPE_URL -> {
                                val title = barcode.url?.getTitle()
                                val url = barcode.url?.getUrl()
                                Toast.makeText(this@Scanner, "Type: URL ${title} $url", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }                        }
                .addOnFailureListener {
                    Toast.makeText(this@Scanner, "Failed $it", Toast.LENGTH_SHORT).show()
                }

    }
    fun connection (barcode:Long) {
        val call: Call<Response> = api.getData(barcode)
        call.enqueue(object : Callback<Response>{
            override fun onResponse(call: Call<Response>?, response: retrofit2.Response<Response>?) {
                if(!response?.body()?.status.equals("product found")) {
                    Toast.makeText(this@Scanner, "${response?.body()?.status}", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@Scanner, "HELLO", Toast.LENGTH_SHORT).show()
                    val brandName=response?.body()?.product?.brandsTags?.get(0)!!
                    val energy=response?.body()?.product?.nutriments?.energy!!.toLong()
                    val saturatedFat=response?.body()?.product?.nutriments?.saturatedFat!!.toLong()
                    val sugars=response?.body()?.product?.nutriments?.sugars!!.toLong()
                    val carbohydrates=response?.body()?.product?.nutriments?.carbohydrates!!.toLong()
                        updateViews(brandName,energy,saturatedFat,sugars,carbohydrates)
                    confirm.setOnClickListener {
                        addData(barcode,brandName,energy,saturatedFat,sugars,carbohydrates)
                        finish()
                    }
                }
            }
            override fun onFailure(call: Call<Response>?, t: Throwable?) {
                Toast.makeText(this@Scanner, "FAILED $t", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun addData(productBarcode:Long,name:String,energy :Long,saturatedFat:Long,sugars:Long,carbohydrates:Long){
        AppExecutors.instance?.diskIO()?.execute {
            val JFTDatabase: JFTDatabase = JFTDatabase.getInstance(applicationContext)
            var isBarcodeExisted= JFTDatabase.productDao().selectProductWithBarcode(productBarcode)
            if (isBarcodeExisted==0.toLong()){
                val ProductEntry = ProductEntity(productBarcode,name,energy,saturatedFat,sugars,carbohydrates)
                JFTDatabase.productDao().insertProduct(ProductEntry)
                val productObject = JFTDatabase.productDao().loadLastRow()
                isBarcodeExisted= productObject.barcode
            }
            val userName = fbAuth.currentUser?.displayName
            val userEmail= fbAuth.currentUser?.email
            var isIdExisted = JFTDatabase.userDao().selectUserWithEmail(userEmail!!)
            if(isIdExisted==0) {
                val user = UserEntity(userName!!, userEmail!!)
                JFTDatabase.userDao().insertUser(user)
                val userObject = JFTDatabase.userDao().loadLastRow()
                isIdExisted = userObject.id
            }
            val sameUserProduct = JFTDatabase.upDao().sameUserSameProduct(isIdExisted,isBarcodeExisted)
            if (sameUserProduct!=0){
                var counterTobeUpdated = JFTDatabase.upDao().loadToUpdateCounter(isIdExisted,isBarcodeExisted)
                counterTobeUpdated++
                JFTDatabase.upDao().Update(counterTobeUpdated,isIdExisted,isBarcodeExisted)
            }
            else {
                val date = Date()
                val userProductEntry = UserProductEntity(isIdExisted, isBarcodeExisted, 1, date)
                JFTDatabase.upDao().insertup(userProductEntry)
            }
        }
    }
    @SuppressLint("SetTextI18n")
    fun updateViews(name:String, energy :Long, saturatedFat:Long, sugars:Long, carbohydrates:Long){
        dataLayout.visibility=View.VISIBLE
        tvBrandTag.text=getString(R.string.brand_name)+ name
        tvEnergy.text=getString(R.string.energy)+energy.toString()+getString(R.string.energy_unit)
        tvSaturatedFat.text=getString(R.string.saturated_fat)+saturatedFat.toString() +getString(R.string.unit)
        tvSugars.text=getString(R.string.sugars)+sugars.toString()+getString(R.string.unit)
        tvCarbohydrates.text=getString(R.string.carbohydrates)+carbohydrates.toString() +getString(R.string.unit)
    }
}