package com.example.rawan.junkfoodtracker.Today.View

import com.example.rawan.junkfoodtracker.Room.BrandNameAndCounter
import com.example.rawan.junkfoodtracker.Room.JFTDatabase
import com.example.rawan.junkfoodtracker.Room.NutritionInfo

/**
 * Created by rawan on 11/10/18.
 */
interface TodayView{
    fun acceptNutritionInfo(nutrition: NutritionInfo)
    fun acceptUsersProductList(productList:List<BrandNameAndCounter>)
    fun exception(e:Throwable)
}