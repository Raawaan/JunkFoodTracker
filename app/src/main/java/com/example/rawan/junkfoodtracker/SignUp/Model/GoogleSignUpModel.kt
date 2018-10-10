package com.example.rawan.junkfoodtracker.SignUp.Model

import android.widget.Toast
import com.example.rawan.junkfoodtracker.R
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

/**
 * Created by rawan on 10/10/18.
 */
class GoogleSignUpModel(private var fbAuth: FirebaseAuth){
    fun handleGoogleResults(completedTask: Task<GoogleSignInAccount>,onSuccess:()->Unit, onFailed:(s: java.lang.Exception)->Unit){
        try {
            val account = completedTask.getResult(ApiException::class.java)
            fireBaseAuthWithGoogle(account,onSuccess = {
                onSuccess()
            },onFailed = {
                onFailed(it)
            })
        } catch (e: Exception) {
            onFailed(e)
        }
    }
    private fun fireBaseAuthWithGoogle(acct: GoogleSignInAccount,onSuccess:()->Unit, onFailed:(s: java.lang.Exception)->Unit) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        fbAuth.signInWithCredential(credential).addOnCompleteListener {
            if (!it.isSuccessful)
                onFailed(it.exception!!)
            else {
                onSuccess()
            }
        }
    }
}