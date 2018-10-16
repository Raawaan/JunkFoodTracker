package com.example.rawan.junkfoodtracker.SignUp.Presenter

import android.util.Patterns
import com.example.rawan.junkfoodtracker.R
import com.example.rawan.junkfoodtracker.SignUp.Model.*
import com.example.rawan.junkfoodtracker.SignUp.View.SignUpView
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import java.util.regex.Pattern

/**
 * Created by rawan on 10/10/18.
 */
class SignUpPresenterImp(private val fireBaseSignUpModel: FireBaseSignUpModel, private val googleSignUpModel: GoogleSignUpModel,
                         private val facebookSignUpModel: FacebookSignUpModel, private val signUpView: SignUpView) : SignUpPresenter {

    override fun handleFacebookSignUpResults(token: AccessToken) {
        facebookSignUpModel.signUpWithFacebook(token, onSuccess = {
            signUpView.onSignUpSuccess()
        }, onFailed = {
            signUpView.onSignUpFailed(it)
        })
    }
    override fun handleGoogleSignUpResults(completedTask: Task<GoogleSignInAccount>) {
        googleSignUpModel.handleGoogleResults(completedTask, onFailed = {
            signUpView.onSignUpFailed(it)
        }, onSuccess = {
            signUpView.onSignUpSuccess()
        })
    }
    override fun createFireBaseAccount(email: String, password: String) {
        if (!email.isValidEmail()) {
            signUpView.onEmailValidationFail(R.string.invalidEmail)
            return
        }
        if (!password.isValidPassword()) {
            signUpView.onPasswordValidationFail(R.string.invalidPassword)
            return
        }
        fireBaseSignUpModel.signUpWithFireBase(email, password, onSuccess = {
            signUpView.onSignUpSuccess()
        }, onFailed = {
            signUpView.onSignUpFailed(it)
        })
    }
    override fun validateEmail(email: String) {
        if (!email.isValidEmail()) {
            signUpView.onEmailValidationFail(R.string.invalidEmail)
        }
        else
            signUpView.onEmailValidationSuccess()
    }
    override fun validatePassword(password: String) {
        if (!password.isValidPassword()) {
            signUpView.onPasswordValidationFail(R.string.invalidPassword)
        }
        else
            signUpView.onPasswordValidationSuccess()
    }
}

fun String.isValidEmail(): Boolean {
    val ePattern = Patterns.EMAIL_ADDRESS
    val p = java.util.regex.Pattern.compile(ePattern.toString())
    val m = p.matcher(this)
    return m.matches()
}

fun String.isValidPassword(): Boolean {
    val regex = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}")
    val p = java.util.regex.Pattern.compile(regex.toString())
    val m = p.matcher(this)
    return m.matches()
}