package com.example.rawan.junkfoodtracker.Today.Presenter

import com.example.rawan.junkfoodtracker.Room.JFTDatabase

/**
 * Created by rawan on 11/10/18.
 */
interface TodayPresenter {
    fun requestCurrentUserNutritionInfo()
    fun requestCurrentUserProductsList()
}