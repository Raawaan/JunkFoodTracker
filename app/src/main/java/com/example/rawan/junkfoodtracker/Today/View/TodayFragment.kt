package com.example.rawan.junkfoodtracker.Today.View

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.todolist.AppExecutors
import com.example.rawan.junkfoodtracker.ProductAdapter
import com.example.rawan.junkfoodtracker.R
import com.example.rawan.junkfoodtracker.Room.BrandNameAndCounter
import com.example.rawan.junkfoodtracker.Room.DateWithoutTime
import com.example.rawan.junkfoodtracker.Room.JFTDatabase
import com.example.rawan.junkfoodtracker.Room.NutritionInfo
import com.example.rawan.junkfoodtracker.Today.Model.TodayModel
import com.example.rawan.junkfoodtracker.Today.Presenter.TodayPresenter
import com.example.rawan.junkfoodtracker.Today.Presenter.TodayPresenterImp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.today_fragment.*
import java.util.*

/**
 * Created by rawan on 09/09/18.
 */
class TodayFragment : android.support.v4.app.Fragment(),TodayView {
    lateinit var productAdapter: ProductAdapter
    lateinit var listProducts: List<BrandNameAndCounter>
    lateinit var todayPresenter:TodayPresenter
    override fun onStart() {
        super.onStart()
        todayPresenter=TodayPresenterImp(TodayModel(FirebaseAuth.getInstance(), JFTDatabase.getInstance(activity!!.applicationContext)),this)
        todayPresenter.requestCurrentUserNutritionInfo()
        todayPresenter.requestCurrentUserProductsList()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.today_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todayPresenter=TodayPresenterImp(TodayModel(FirebaseAuth.getInstance(), JFTDatabase.getInstance(activity!!.applicationContext)),this)
        todayPresenter.requestCurrentUserProductsList()
    }
    override fun acceptNutritionInfo(nutrition: NutritionInfo) {
        updateViews(nutrition.energy, nutrition.saturatedFat, nutrition.sugars, nutrition.carbohydrates)
    }
    override fun acceptUsersProductList(productList: List<BrandNameAndCounter>) {
        listProducts =productList
        myRVItem.layoutManager = LinearLayoutManager(activity)
        productAdapter = ProductAdapter(listProducts)
        myRVItem.adapter = productAdapter
    }
    private fun updateViews(energy: Long, saturatedFat: Long, sugars: Long, carbohydrates: Long) {
        val shredPref=PreferenceManager.getDefaultSharedPreferences(activity)
        fragTvEnergy.append(energy.toString() + getString(R.string.energy_unit))
        if (energy > shredPref.getString(getString(R.string.energy_key),getString(R.string.energy_default)).toInt())
            fragTvEnergy.setTextColor(Color.parseColor(getString(R.string.red)))
        fragTvSaturatedFat.append(saturatedFat.toString() + getString(R.string.unit))
        if (saturatedFat > shredPref.getString(getString(R.string.saturated_fat_key),getString(R.string.saturatedFat_default)).toInt())
            fragTvSaturatedFat.setTextColor(Color.parseColor(getString(R.string.red)))
        fragTvSugars.append(sugars.toString() + getString(R.string.unit))
        if (sugars > shredPref.getString(getString(R.string.sugars_key),getString(R.string.sugars_default)).toInt())
            fragTvSugars.setTextColor(Color.parseColor(getString(R.string.red)))
        fragTvCarbohydrates.append(carbohydrates.toString() + getString(R.string.unit))
        if (carbohydrates > shredPref.getString(getString(R.string.carbohydrates_key),getString(R.string.carbohydrates_default)).toInt())
            fragTvCarbohydrates.setTextColor(Color.parseColor(getString(R.string.red)))
    }
}
//        val shredPref=PreferenceManager.getDefaultSharedPreferences(activity)
//                val editor :SharedPreferences.Editor=shredPref.edit()
//        editor.putString("energy","2000")
//        editor.apply()
