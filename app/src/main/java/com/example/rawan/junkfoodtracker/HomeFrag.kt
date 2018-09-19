package com.example.rawan.junkfoodtracker

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.android.todolist.AppExecutors
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.home_frag.*
import kotlinx.android.synthetic.main.scanner.*

/**
 * Created by rawan on 09/09/18.
 */
class HomeFrag:android.support.v4.app.Fragment(){
    val fbAuth= FirebaseAuth.getInstance()
    companion object {
        fun newInstance():HomeFrag {
            return HomeFrag()
        }
    }

    override fun onStart() {
        super.onStart()

        val JFTDatabase = com.example.rawan.roomjft.Room.JFTDatabase.getInstance(activity!!.applicationContext)
        val userID = JFTDatabase.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
        val nutrition = JFTDatabase.upDao().selectSummationOfNutInfo(userID)
        updateViews(nutrition.energy,nutrition.saturatedFat,nutrition.sugars,nutrition.carbohydrates)
        listProducts =JFTDatabase.upDao().selectProductsOfCurrentUSer(userID)
        myRVItem.layoutManager = LinearLayoutManager(activity)
        productAdapter = ProductAdapter(listProducts)
        myRVItem.adapter = productAdapter
    }
    lateinit var productAdapter: ProductAdapter
    lateinit var listProducts: List<String>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_frag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppExecutors.instance?.diskIO()?.execute {
            val JFTDatabase = com.example.rawan.roomjft.Room.JFTDatabase.getInstance(activity!!.applicationContext)
            val userID = JFTDatabase.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
            listProducts =JFTDatabase.upDao().selectProductsOfCurrentUSer(userID)
            myRVItem.layoutManager = LinearLayoutManager(activity)
            productAdapter = ProductAdapter(listProducts)
            myRVItem.adapter = productAdapter
        }
    }
    fun updateViews(energy :Long, saturatedFat:Long, sugars:Long, carbohydrates:Long){
        fragTvEnergy.text=getString(R.string.energy)+energy.toString()+getString(R.string.energy_unit)
        fragTvSaturatedFat.text=getString(R.string.saturated_fat)+saturatedFat.toString() +getString(R.string.unit)
        fragTvSugars.text=getString(R.string.sugars)+sugars.toString()+getString(R.string.unit)
        fragTvCarbohydrates.text=getString(R.string.carbohydrates)+carbohydrates.toString() +getString(R.string.unit)
    }
}
