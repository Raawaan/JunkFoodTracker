package com.example.rawan.junkfoodtracker.SignUp.Model

import android.widget.Toast
import com.example.rawan.junkfoodtracker.R
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

/**
 * Created by rawan on 10/10/18.
 */
class FacebookSignUpModel(private var fbAuth: FirebaseAuth){
    fun signUpWithFacebook(token: AccessToken, onSuccess:()->Unit, onFailed:(s: Exception)->Unit){
        val credential = FacebookAuthProvider.getCredential(token.token)
        fbAuth.signInWithCredential(credential).addOnCompleteListener {
            if (!it.isSuccessful)
                onFailed(it.exception!!)
            else {
                onSuccess()
            }
        }
    }
}