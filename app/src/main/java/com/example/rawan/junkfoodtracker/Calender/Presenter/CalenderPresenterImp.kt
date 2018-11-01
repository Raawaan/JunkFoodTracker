package com.example.rawan.junkfoodtracker.Calender.Presenter

import com.example.rawan.junkfoodtracker.Calender.Model.CalenderModel
import com.example.rawan.junkfoodtracker.Calender.View.CalenderView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by rawan on 11/10/18.
 */
class CalenderPresenterImp(private val calenderModel: CalenderModel, private val calendarView: CalenderView) : CalenderPresenter {
    private val compositeDisposable=CompositeDisposable()

    override fun onDetach() {
        compositeDisposable.clear()
    }

    override fun selectNutritionInfoOfSelectedPeriod(from: Long, to: Long) {
        compositeDisposable.add(calenderModel.selectNunInfoOfSelectedPeriod(from, to).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeBy(onNext = {
                    calendarView.acceptNutInfoOfSelectedPeriod(it)
                }, onError = {
                    calendarView.exception(it)
                })
        )
    }
    override fun selectMinDateFromPicker() {
        compositeDisposable.add(calenderModel.getMinDate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeBy(onSuccess = {
                    calendarView.acceptMinDateFromPicker(it)
                }, onError = {
                    calendarView.exception(it)
                })
        )
    }
    override fun selectMinDateToPicker() {
        compositeDisposable.add(calenderModel.getMinDate().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeBy(onSuccess = {
                    calendarView.acceptMinDateToPicker(it)
                }, onError = {
                    calendarView.exception(it)
                })
        )
    }

    override fun requestCurrentUserNutritionInfo(date: Long) {
        compositeDisposable.add(calenderModel.requestUserNutritionInformation(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeBy(onNext = {
                    calendarView.acceptNutritionInfo(it)
                }, onError = {
                    calendarView.exception(it)
                })
        )
    }
    override fun requestCurrentUserProductsList(date: Long) {
        compositeDisposable.add(calenderModel.requestUserProductsList(date).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeBy(onNext = {
                    calendarView.acceptUsersProductList(it)
                }, onError = {
                    calendarView.exception(it)
                })
        )
    }
    override fun selectMinDate() {
        compositeDisposable.add(calenderModel. getMinDate().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeBy(onSuccess = {
                    calendarView.acceptMinDate(it)
                }, onError = {
                    calendarView.exception(it)
                })
        )
    }
}