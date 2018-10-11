package com.example.rawan.junkfoodtracker.SignIn.Presenter

import com.example.rawan.junkfoodtracker.R
import com.example.rawan.junkfoodtracker.SignIn.Model.FacebookSignInModel
import com.example.rawan.junkfoodtracker.SignIn.Model.FireBaseSignInModel
import com.example.rawan.junkfoodtracker.SignIn.Model.GoogleSignInModel
import com.example.rawan.junkfoodtracker.SignIn.View.SignInView
import com.example.rawan.junkfoodtracker.SignUp.Presenter.isValidEmail
import com.example.rawan.junkfoodtracker.SignUp.Presenter.isValidPassword
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

/**
 * Created by rawan on 11/10/18.
 */
class SignInPresenterImp(private val fireBaseSignInModel: FireBaseSignInModel,
                         private val facebookSignInModel: FacebookSignInModel,
                         private val googleSignInModel: GoogleSignInModel,
                         private val signInView: SignInView):SignInPresenter{
    override fun handleGoogleSignInResults(completedTask: Task<GoogleSignInAccount>) {
        googleSignInModel.handleResult(completedTask,onSuccess = {
            signInView.onSignInSuccess()
        },onFailedNoExp = {
            signInView.onSignInFailed(R.string.authentication_issue)
        },onFailed = {
            signInView.onSignInFailedWithExeption(it)
        })
    }

    override fun handleFacebookSignInResults(token: AccessToken) {
        facebookSignInModel.signUpWithFacebook(token,
                onFailed = {
                    signInView.onSignInFailed(R.string.authentication_issue)
                },
                onSuccess = {
                    signInView.onSignInSuccess()
                })
    }
    override fun fireBaseSignInAccount(email: String, password: String) {
        if (!email.isValidEmail()) {
            signInView.onEmailValidationFail(R.string.invalidEmail)
            return }
        else
            signInView.onEmailValidationSuccess()

        if (!password.isValidPassword()) {
            signInView.onPasswordValidationFail(R.string.invalidPassword)
            return
        }
        else
            signInView.onPasswordValidationSuccess()
        fireBaseSignInModel.signUpWithFireBase(email,password,
                onSuccess = {
                    signInView.onSignInSuccess()
                },
                onFailed = {
            signInView.onSignInFailed(R.string.invalidEmailPass)
        })
    }

}