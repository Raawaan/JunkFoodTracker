package com.example.rawan.junkfoodtracker.SignIn.Model

import android.widget.Toast
import com.example.rawan.junkfoodtracker.R
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

/**
 * Created by rawan on 11/10/18.
 */
class GoogleSignInModel(private var fbAuth: FirebaseAuth){
     fun handleResult(completedTask: Task<GoogleSignInAccount>,onSuccess: () -> Unit, onFailed: (e:Exception) -> Unit,onFailedNoExp: () -> Unit) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            fireBaseAuthWithGoogle(account,onSuccess = {
                onSuccess()
            },onFailed = {
                onFailedNoExp()
            })
        } catch (e: Exception) {
            onFailed(e)
        }
     }
    private fun fireBaseAuthWithGoogle(acct: GoogleSignInAccount, onSuccess: () -> Unit, onFailed: () -> Unit) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        fbAuth.signInWithCredential(credential).addOnCompleteListener {
            if (!it.isSuccessful)
                onFailed()
            else
                onSuccess()
        }
    }
}