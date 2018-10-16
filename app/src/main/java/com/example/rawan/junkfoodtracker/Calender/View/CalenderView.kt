package com.example.rawan.junkfoodtracker.Calender.View

import com.example.rawan.junkfoodtracker.Room.BrandNameAndCounter
import com.example.rawan.junkfoodtracker.Room.NutritionInfo

/**
 * Created by rawan on 11/10/18.
 */
interface CalenderView {
    fun acceptMinDate(minDate:Long)
    fun acceptNutInfoOfSelectedPeriod(nutrition: NutritionInfo)
    fun acceptMinDateFromPicker(minDate:Long)
    fun acceptMinDateToPicker(minDate:Long)
    fun acceptNutritionInfo(nutrition: NutritionInfo)
    fun acceptUsersProductList(productList:List<BrandNameAndCounter>)
}