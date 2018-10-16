package com.example.rawan.junkfoodtracker.Calender.Presenter

import com.example.rawan.junkfoodtracker.AsyncTaskJFT
import com.example.rawan.junkfoodtracker.Calender.Model.CalenderModel
import com.example.rawan.junkfoodtracker.Calender.View.CalenderView

/**
 * Created by rawan on 11/10/18.
 */
class CalenderPresenterImp(private val calenderModel: CalenderModel,private val calendarView: CalenderView):CalenderPresenter{
    override fun selectNutritionInfoOfSelectedPeriod(from: Long, to: Long) {
        AsyncTaskJFT(inBackground = {
             return@AsyncTaskJFT calenderModel.selectNunInfoOfSelectedPeriod(from,to)
        },onSuccess = {
                calendarView.acceptNutInfoOfSelectedPeriod(it)
        }).execute()
    }
    override fun selectMinDateFromPicker() {
        AsyncTaskJFT(inBackground = {
           return@AsyncTaskJFT calenderModel.getMinDate()
        },onSuccess = {
            calendarView.acceptMinDateFromPicker(it)
        }).execute()
    }
    override fun selectMinDateToPicker() {
        AsyncTaskJFT(inBackground = {
        calenderModel.getMinDate()
    },onSuccess = {
            calendarView.acceptMinDateToPicker(it)
        }).execute()
    }
    override fun requestCurrentUserNutritionInfo(date:Long) {
        AsyncTaskJFT(inBackground = {
            calenderModel.requestUserNutritionInformation(date)
            },onSuccess = {
            calendarView.acceptNutritionInfo(it)
        }).execute()
    }
    override fun requestCurrentUserProductsList(date:Long) {
        AsyncTaskJFT(inBackground = {
            calenderModel.requestUserProductsList(date)
        },onSuccess = {
            calendarView.acceptUsersProductList(it)
        }).execute()
    }

    override fun selectMinDate() {
            AsyncTaskJFT(inBackground = {
                calenderModel.getMinDate()
                },onSuccess = {
            calendarView.acceptMinDate(it)
        }).execute()
    }

}