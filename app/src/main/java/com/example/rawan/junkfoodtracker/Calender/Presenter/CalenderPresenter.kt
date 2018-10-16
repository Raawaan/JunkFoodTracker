package com.example.rawan.junkfoodtracker.Calender.Presenter

/**
 * Created by rawan on 11/10/18.
 */
interface CalenderPresenter{
    fun selectMinDate()
    fun selectMinDateFromPicker()
    fun selectMinDateToPicker()
    fun selectNutritionInfoOfSelectedPeriod(from:Long,to:Long)
    fun requestCurrentUserNutritionInfo(date:Long)
    fun requestCurrentUserProductsList(date:Long)
}