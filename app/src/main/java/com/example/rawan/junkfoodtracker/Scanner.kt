package com.example.rawan.junkfoodtracker

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.zxing.Result
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.scanner.*
import java.io.IOException
import android.support.annotation.NonNull
import com.example.android.todolist.AppExecutors
import com.example.rawan.roomjft.Room.JFTDatabase
import com.example.rawan.roomjft.Room.ProductEntity
import com.example.rawan.roomjft.Room.UserEntity
import com.example.rawan.roomjft.Room.UserProductEntity
import com.facebook.stetho.Stetho
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import java.util.*


/**
 * Created by rawan on 10/09/1b.
 */
class Scanner:AppCompatActivity(){
    companion object {
        val CHOOSE_IMAGE_REQUEST=10
    }
   lateinit var  image:FirebaseVisionImage
    private var imageBitmap: Bitmap?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner)
        setSupportActionBar(Scannertoolbar)
        Stetho.initializeWithDefaults(this)

        supportActionBar?.title = "Scanner"
        choose_img.setOnClickListener {
    chooseImage()
        }
        insert.setOnClickListener {
            AppExecutors.instance?.diskIO()?.execute {
                val JFTDatabase: JFTDatabase = JFTDatabase.getInstance(applicationContext)
                val date = Date()
                val ProductEntry = ProductEntity("Kitkat", 19, 12, 13, 12, 19)
                JFTDatabase.productDao().insertProduct(ProductEntry)
                val productObject = JFTDatabase.productDao().loadLastRow()
                val idOfProduct = productObject.pid
                val User = UserEntity("s", "s")
                JFTDatabase.userDao().insertUser(User)
                val userObject = JFTDatabase.userDao().loadLastRow()
                val idOfUser = userObject.id

                val userProductEntry = UserProductEntity(idOfUser, idOfProduct, 12, date)
                JFTDatabase.upDao().insertup(userProductEntry)
                JFTDatabase.upDao().loadAllUsersWithProducts()
                JFTDatabase.upDao().loadAllData()
                JFTDatabase.upDao().loadAllUsersWithProducts()
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==CHOOSE_IMAGE_REQUEST&&resultCode== Activity.RESULT_OK){
            val photo= data!!.extras.get("data") as Bitmap
            barcodeReg(photo)
//
        }
    }

    fun chooseImage(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CHOOSE_IMAGE_REQUEST)
    }
    fun barcodeReg(photo:Bitmap){
      iv_barcode.setImageBitmap(photo)
        image = FirebaseVisionImage.fromBitmap(photo)
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
                                        val valu = barcode.displayValue
                                        Toast.makeText(this@Scanner, "PRODUCT $valu", Toast.LENGTH_SHORT).show()
                                    }
                                    FirebaseVisionBarcode.TYPE_WIFI -> {
                                        val ssid = barcode.wifi?.getSsid()
                                        val password = barcode.wifi?.getPassword()
                                        val type = barcode.wifi?.getEncryptionType()
                                        Toast.makeText(this@Scanner, "wifi ${ssid}  $type", Toast.LENGTH_SHORT).show()
                                    }
                                    FirebaseVisionBarcode.TYPE_URL -> {
                                        val title = barcode.url?.getTitle()
                                        val url = barcode.url?.getUrl()
                                        Toast.makeText(this@Scanner, "URL ${title} $url", Toast.LENGTH_SHORT).show()
                                    }
                                }


                            }                        }
                        .addOnFailureListener {
                            Toast.makeText(this@Scanner, "Failed $it", Toast.LENGTH_SHORT).show()
                        }

    }

}