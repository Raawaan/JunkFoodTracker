package com.example.rawan.junkfoodtracker.SignIn.Model

import android.widget.Toast
import com.example.rawan.junkfoodtracker.R
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by rawan on 11/10/18.
 */
class FireBaseSignInModel(private var fbAuth: FirebaseAuth) {
    fun signUpWithFireBase(email: String, password: String, onSuccess: () -> Unit, onFailed: () -> Unit) {
        fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful)
                onSuccess()
            else
                onFailed()
        }

    }


}