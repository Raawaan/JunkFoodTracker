package com.example.rawan.junkfoodtracker.Scanner.View

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
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.scanner.*
import com.facebook.stetho.Stetho
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import android.Manifest
import com.example.rawan.junkfoodtracker.util.DatePickerDialogUtility
import com.example.rawan.junkfoodtracker.InternetConnection
import com.example.rawan.junkfoodtracker.R
import com.example.rawan.junkfoodtracker.Room.*
import com.example.rawan.junkfoodtracker.Scanner.Model.localAccessModel.LocalScannerModel
import com.example.rawan.junkfoodtracker.Scanner.Model.remoteAccessModel.ProductInfoResponseModel
import com.example.rawan.junkfoodtracker.Scanner.Presenter.ScannerPresenter
import com.example.rawan.junkfoodtracker.Scanner.Presenter.ScannerPresenterImp
import java.text.SimpleDateFormat


/**
 * Created by rawan on 10/09/1b.
 */
class ScannerActivity : AppCompatActivity(), ScannerView {
    private val fbAuth = FirebaseAuth.getInstance()
    var counterPM = 1
    private val calender = Calendar.getInstance()
    val date = Date()
    var selectedDate = Date()
    private val CAMERA_REQUEST_CODE = 123
    lateinit var scannerPresenter: ScannerPresenter
    var photo: Bitmap?=null
    lateinit var JFTDatabase: JFTDatabase
    lateinit var productEntityL: ProductEntity

    companion object {
        const val CHOOSE_IMAGE_REQUEST = 10
    }

    lateinit var image: FirebaseVisionImage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner)
        setSupportActionBar(Scannertoolbar)
        supportActionBar?.title = getString(R.string.scanner_activity_name)
        scannerPresenter = ScannerPresenterImp(LocalScannerModel(com.example.rawan.junkfoodtracker.Room.JFTDatabase.getInstance(this)),
                ProductInfoResponseModel(), this)
        choose_img.setOnClickListener { setupPermissions() }
        ivDate.setOnClickListener {
            DatePickerDialogUtility.create(context = this,onPickDate = { date, year, month, day->
                selectedDate = date
                dateTitle.text = getString(R.string.date_format,day.toString(),month.plus(1).toString()
                        ,year.toString())
            }).show()
        }
        plusFab.setOnClickListener {
            if (counterPM != 10) {
                counterPM++
                counterPlusMinus.text = counterPM.toString()
            } else
                Toast.makeText(this@ScannerActivity, getString(R.string.warning_plus), Toast.LENGTH_SHORT).show()
        }
        minusFab.setOnClickListener {
            if (counterPM != 1) {
                counterPM--
                counterPlusMinus.text = counterPM.toString()
            } else
                Toast.makeText(this@ScannerActivity, getString(R.string.warning_minus), Toast.LENGTH_SHORT).show()
        }
        confirm.setOnClickListener {
            scannerPresenter.addProduct(productEntityL.barcode, productEntityL.brandName,
                    productEntityL.energy, productEntityL.saturatedFat,
                    productEntityL.sugars, productEntityL.carbohydrates, selectedDate, fbAuth.currentUser?.email!!,
                    fbAuth.currentUser?.displayName!!, counterPM)
            finish()
            Toast.makeText(this@ScannerActivity, getString(R.string.enjoy), Toast.LENGTH_SHORT).show()
        }
    }

    private fun barcodeDetection(photo: Bitmap) {
        iv_barcode.background = null
        iv_barcode.setImageBitmap(photo)
        if (InternetConnection.isOnline(this@ScannerActivity))
            scannerPresenter.detectBarcode(photo)
        else
            Toast.makeText(this@ScannerActivity, getString(R.string.connection), Toast.LENGTH_SHORT).show()
    }
    override fun updateViewResponse(productEntity: ProductEntity) {
        updateViews(productEntity.brandName, productEntity.energy,
                productEntity.saturatedFat, productEntity.sugars, productEntity.carbohydrates)
        productEntityL = ProductEntity(productEntity.barcode, productEntity.brandName, productEntity.energy,
                productEntity.saturatedFat, productEntity.sugars, productEntity.carbohydrates)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = data!!.extras.get("data") as? Bitmap
           photo?.let {
               barcodeDetection(it)
           }
        }
    }

    private fun chooseImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CHOOSE_IMAGE_REQUEST)
    }

    private fun updateViews(name: String, energy: Long, saturatedFat: Long, sugars: Long, carbohydrates: Long) {
        dataLayout.visibility = View.VISIBLE
        val dateFormat = SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH)
        dateTitle.text = dateFormat.format(calender.time)
        counterPlusMinus.text = counterPM.toString()
        tvBrandTag.text = getString(R.string.brand_name, name)
        tvEnergy.text = getString(R.string.energy, energy.toString(), getString(R.string.energy_unit))
        tvSaturatedFat.text = getString(R.string.saturated_fat, saturatedFat.toString(), getString(R.string.unit))
        tvSugars.text = getString(R.string.sugars, sugars.toString(), getString(R.string.unit))
        tvCarbohydrates.text = getString(R.string.carbohydrates, carbohydrates.toString(), getString(R.string.unit))
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED)
            makeRequest()
        else
            chooseImage()
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onSuccess(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun onFailed(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                else
                    chooseImage()
            }
        }
    }

}