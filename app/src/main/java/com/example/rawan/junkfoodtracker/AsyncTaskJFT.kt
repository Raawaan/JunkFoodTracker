package com.example.rawan.junkfoodtracker

import android.os.AsyncTask

/**
 * Created by rawan on 15/10/18.
 */
class AsyncTaskJFT<T>(private val inBackground:()->T,private val onSuccess:(T)->Unit):AsyncTask<Any,Any,T>(){
    override fun doInBackground(vararg params: Any?): T {
        return inBackground()
    }
    override fun onPostExecute(result: T) {
        super.onPostExecute(result)
        onSuccess(result)

    }

}