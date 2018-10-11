package com.example.rawan.junkfoodtracker.SignIn.Presenter

import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

/**
 * Created by rawan on 11/10/18.
 */
interface SignInPresenter {
    fun fireBaseSignInAccount(email:String, password:String)
    fun handleFacebookSignInResults(token: AccessToken)
    fun handleGoogleSignInResults(completedTask: Task<GoogleSignInAccount>)


}