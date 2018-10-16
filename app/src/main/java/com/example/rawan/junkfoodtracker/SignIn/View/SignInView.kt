package com.example.rawan.junkfoodtracker.SignIn.View

import android.support.annotation.StringRes

/**
 * Created by rawan on 11/10/18.
 */
interface SignInView {
    fun onSignInSuccess()
    fun onSignInFailed(@StringRes errorMsg: Int)
    fun onSignInFailedWithException(e:Exception)
    fun onEmailValidationFail(@StringRes errorMsg: Int)
    fun onEmailValidationSuccess()
    fun onPasswordValidationFail(@StringRes errorMsg: Int)
    fun onPasswordValidationSuccess()
}