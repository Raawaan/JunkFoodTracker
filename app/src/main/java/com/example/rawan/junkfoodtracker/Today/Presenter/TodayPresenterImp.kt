package com.example.rawan.junkfoodtracker.Today.Presenter

import com.example.rawan.junkfoodtracker.AsyncTaskJFT
import com.example.rawan.junkfoodtracker.Room.JFTDatabase
import com.example.rawan.junkfoodtracker.Today.Model.TodayModel
import com.example.rawan.junkfoodtracker.Today.View.TodayView

/**
 * Created by rawan on 11/10/18.
 */
class TodayPresenterImp(private val todayModel: TodayModel,private val todayView: TodayView):TodayPresenter{
    override fun requestCurrentUserProductsList() {
        AsyncTaskJFT(inBackground = {
            todayModel.requestUserProductsList()
        },onSuccess = {
            todayView.acceptUsersProductList(it)
        }).execute()
    }
    override fun requestCurrentUserNutritionInfo() {
        AsyncTaskJFT(inBackground = {
            todayModel.requestUserNutritionInformation()
        },onSuccess = {
            todayView.acceptNutritionInfo(it)
        }).execute()
    }
}