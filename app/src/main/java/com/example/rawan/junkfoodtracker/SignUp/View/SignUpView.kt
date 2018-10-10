package com.example.rawan.junkfoodtracker.SignUp.View

import android.support.annotation.StringRes

/**
 * Created by rawan on 10/10/18.
 */
interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailed(e: Exception)
    fun onEmailValidationFail(@StringRes errorMsg: Int)
    fun onEmailValidationSuccess()
    fun onPasswordValidationFail(@StringRes errorMsg: Int)
    fun onPasswordValidationSuccess()
}