package com.example.rawan.junkfoodtracker.SignUp.Model

import android.provider.Settings.Global.getString
import android.util.Patterns
import android.widget.Toast
import com.example.rawan.junkfoodtracker.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.sign_up.*
import java.lang.Exception
import java.util.regex.Pattern

/**
 * Created by rawan on 10/10/18.
 */
class FireBaseSignUpModel(private var fbAuth: FirebaseAuth) {
    fun signUpWithFireBase(email: String, password: String, onSuccess: () -> Unit, onFailed: (s: Exception) -> Unit) {
        fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            val user = fbAuth.currentUser
            val profile = UserProfileChangeRequest.Builder().setDisplayName(email.removeRange(email.indexOf("@"), email.length)).build()
            user?.updateProfile(profile)
            if (it.isSuccessful) {
                onSuccess()
            } else
                onFailed(it.exception!!)
        }
    }


}

