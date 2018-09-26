package com.example.rawan.junkfoodtracker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.scanner.*
import com.example.android.todolist.AppExecutors
import com.example.rawan.junkfoodtracker.Retrofit.API
import com.facebook.stetho.Stetho
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import java.util.*
import android.Manifest
import android.content.Context
import android.widget.Toast.makeText
import com.example.rawan.junkfoodtracker.Room.DateWithoutTime
import com.example.rawan.roomjft.Room.*
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager



/**
 * Created by rawan on 10/09/1b.
 */
class ScannerActivity:AppCompatActivity(),View.OnClickListener{
    val api = API.create()
    val fbAuth= FirebaseAuth.getInstance()
    var counterPM=1
    val date = Date()

    private val CAMERA_REQUEST_CODE=123
    lateinit var photo:Bitmap
    lateinit var JFTDatabase: JFTDatabase
    companion object {
        val CHOOSE_IMAGE_REQUEST=10
    }
    lateinit var  image:FirebaseVisionImage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner)
        setSupportActionBar(Scannertoolbar)
        Stetho.initializeWithDefaults(this)
        supportActionBar?.title = "Scanner"
        choose_img.setOnClickListener(this)
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
        iv_barcode.background=null
        iv_barcode.setImageBitmap(photo)
        val detector = FirebaseVision.getInstance().visionBarcodeDetector
        detector.detectInImage(image)
                .addOnSuccessListener {
                    if (it.size==0){
                        Toast.makeText(this@ScannerActivity, getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }
                    for (barcode in it) {
                        val valueType = barcode.getValueType()
                        Toast.makeText(this@ScannerActivity, "${barcode.displayValue}", Toast.LENGTH_SHORT).show()
                        // See API reference for complete list of supported types
                        when (valueType) {
                            FirebaseVisionBarcode.TYPE_PRODUCT->{
                                if (isOnline(this@ScannerActivity))
                                    connection(barcode.displayValue!!.toLong())
                                else
                                    Toast.makeText(this@ScannerActivity, getString(R.string.connection), Toast.LENGTH_SHORT).show()

                            }
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this@ScannerActivity, "$it", Toast.LENGTH_SHORT).show()
                }

    }
    private fun connection (barcode:Long) {
        val call: Call<Response> = api.getData(barcode)
        call.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>?, response: retrofit2.Response<Response>?) {
                if(!response?.body()?.status.equals(this@ScannerActivity.getString(R.string.product_found))) {
                    Toast.makeText(this@ScannerActivity, "${response?.body()?.status}", Toast.LENGTH_SHORT).show()
                }
                else{
                    val brandName=response?.body()?.product?.brandsTags?.get(0)!!
                    val energy=response.body()?.product?.nutriments?.energy!!.toLong()
                    val saturatedFat=response.body()?.product?.nutriments?.saturatedFat!!.toLong()
                    val sugars=response.body()?.product?.nutriments?.sugars!!.toLong()
                    val carbohydrates=response.body()?.product?.nutriments?.carbohydrates!!.toLong()
                    updateViews(brandName,energy,saturatedFat,sugars,carbohydrates)

                    plusFab.setOnClickListener{
                        counterPM++
                        counterPlusMinus.text=counterPM.toString()}
                    minusFab.setOnClickListener{if (counterPM!=0){
                        counterPM--
                        counterPlusMinus.text=counterPM.toString()
                    }
                    else
                        Toast.makeText(this@ScannerActivity, "Can't be minimized", Toast.LENGTH_SHORT).show()
                    }
                    confirm.setOnClickListener {
                        addData(barcode,brandName,energy,saturatedFat,sugars,carbohydrates,date)
                        Toast.makeText(this@ScannerActivity,getString(R.string.enjoy), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
            override fun onFailure(call: Call<Response>?, t: Throwable?) {
                Toast.makeText(this@ScannerActivity, "$t", Toast.LENGTH_SHORT).show()
            }
        })


    }
    fun addData(productBarcode:Long,name:String,energy :Long,saturatedFat:Long,sugars:Long,carbohydrates:Long,date :Date){
        AppExecutors.instance?.diskIO()?.execute {
            JFTDatabase= com.example.rawan.roomjft.Room.JFTDatabase.getInstance(applicationContext)

            var isBarcodeExisted= JFTDatabase.productDao().selectProductWithBarcode(productBarcode)
            if (isBarcodeExisted==0.toLong()){
                isBarcodeExisted=createNewProductAndGetBarcode(productBarcode,name,energy,saturatedFat,sugars,carbohydrates)
            }
            var isIdExisted = JFTDatabase.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
            if(isIdExisted==0) {
                isIdExisted = createNewUserAndGetId()
            }
            val sameUserProduct = JFTDatabase.upDao().sameUserSameProduct(isIdExisted,isBarcodeExisted,date)
            if (sameUserProduct!=0){
                updateCounter(isIdExisted, isBarcodeExisted)
            }
            else {
                createNewUserProduct(isIdExisted, isBarcodeExisted)
            }
        }
    }
    private fun createNewUserProduct(isIdExisted: Int, isBarcodeExisted: Long) {
        val userProductEntry = UserProductEntity(isIdExisted, isBarcodeExisted, counterPM, date)
        JFTDatabase.upDao().insertup(userProductEntry)
    }
    private fun updateCounter(isIdExisted: Int, isBarcodeExisted: Long) {
        var counterTobeUpdated = JFTDatabase.upDao().loadToUpdateCounter(isIdExisted, isBarcodeExisted)
        counterTobeUpdated += counterPM
        JFTDatabase.upDao().Update(counterTobeUpdated, isIdExisted, isBarcodeExisted)
    }

    @SuppressLint("SetTextI18n")
    fun updateViews(name:String, energy :Long, saturatedFat:Long, sugars:Long, carbohydrates:Long){
        dataLayout.visibility=View.VISIBLE
        counterPlusMinus.text=counterPM.toString()
        tvBrandTag.text=getString(R.string.brand_name)+ name
        tvEnergy.text=getString(R.string.energy)+energy.toString()+getString(R.string.energy_unit)
        tvSaturatedFat.text=getString(R.string.saturated_fat)+saturatedFat.toString() +getString(R.string.unit)
        tvSugars.text=getString(R.string.sugars)+sugars.toString()+getString(R.string.unit)
        tvCarbohydrates.text=getString(R.string.carbohydrates)+carbohydrates.toString() +getString(R.string.unit)
    }
    private fun createNewProductAndGetBarcode(productBarcode:Long,name:String, energy :Long, saturatedFat:Long, sugars:Long, carbohydrates:Long):Long{
        val ProductEntry = ProductEntity(productBarcode,name,energy,saturatedFat,sugars,carbohydrates)
        JFTDatabase.productDao().insertProduct(ProductEntry)
        val productObject = JFTDatabase.productDao().loadLastRow()
        return  productObject.barcode
    }
    private fun createNewUserAndGetId():Int{
        val userName = fbAuth.currentUser?.displayName
        val userEmail= fbAuth.currentUser?.email
        val user = UserEntity(userName!!, userEmail!!)
        JFTDatabase.userDao().insertUser(user)
        val userObject = JFTDatabase.userDao().loadLastRow()
        return userObject.id
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.choose_img-> setupPermissions()
        }
    }
    private fun setupPermissions() {
        val permisison = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
        if(permisison!= PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
        else chooseImage()
    }
    private  fun  makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()

                } else {
                    chooseImage()
                }
            }
        }
    }
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }
}