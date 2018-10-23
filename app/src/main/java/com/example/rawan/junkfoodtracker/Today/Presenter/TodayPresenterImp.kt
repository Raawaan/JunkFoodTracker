package com.example.rawan.junkfoodtracker.Today.Presenter

import com.example.rawan.junkfoodtracker.Today.Model.TodayModel
import com.example.rawan.junkfoodtracker.Today.View.TodayView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by rawan on 11/10/18.
 */
class TodayPresenterImp(private val todayModel: TodayModel, private val todayView: TodayView) : TodayPresenter {
    var compositeDisposable: CompositeDisposable? = CompositeDisposable()
    override fun requestCurrentUserProductsList() {
        compositeDisposable?.add(todayModel.requestUserProductsList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeBy(onNext = {
                    todayView.acceptUsersProductList(it)
                }, onError = {
                    todayView.exception(it)
                })
        )

    }
    override fun requestCurrentUserNutritionInfo() {
        compositeDisposable?.add(todayModel.requestUserNutritionInformation().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy( onNext = {
                    todayView.acceptNutritionInfo(it)
                },onError={
                    todayView.exception(it)
                })
        )
    }
    override fun onDetach() {
        compositeDisposable?.dispose()
    }
}