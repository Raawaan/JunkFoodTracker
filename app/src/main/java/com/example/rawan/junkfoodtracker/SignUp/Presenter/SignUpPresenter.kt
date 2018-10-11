package com.example.rawan.junkfoodtracker.SignUp.Presenter

import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

/**
 * Created by rawan on 10/10/18.
 */
interface SignUpPresenter {
    fun createFireBaseAccount(email:String, password:String)
    fun handleGoogleSignUpResults(completedTask: Task<GoogleSignInAccount>)
    fun handleFacebookSignUpResults(token: AccessToken)
    fun validateEmail(email: String)
    fun validatePassword(password: String)
}
