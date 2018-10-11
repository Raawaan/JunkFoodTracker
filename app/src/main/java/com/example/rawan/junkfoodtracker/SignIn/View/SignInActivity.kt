package com.example.rawan.junkfoodtracker.SignIn.View

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.rawan.junkfoodtracker.HomeActivity
import com.example.rawan.junkfoodtracker.InternetConnection
import com.example.rawan.junkfoodtracker.R
import com.example.rawan.junkfoodtracker.SignIn.Model.FacebookSignInModel
import com.example.rawan.junkfoodtracker.SignIn.Model.FireBaseSignInModel
import com.example.rawan.junkfoodtracker.SignIn.Model.GoogleSignInModel
import com.example.rawan.junkfoodtracker.SignIn.Presenter.SignInPresenter
import com.example.rawan.junkfoodtracker.SignIn.Presenter.SignInPresenterImp
import com.example.rawan.junkfoodtracker.SignUp.View.SignUpActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.main_activity.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import java.util.*


class SignInActivity : AppCompatActivity(),SignInView, View.OnClickListener {



    lateinit var signInPresenter: SignInPresenter
    private val rcSignIn = 9001
    private var mCallbackManager: CallbackManager? = null
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var fbAuth = FirebaseAuth.getInstance()
    override fun onStart() {
        super.onStart()
        if (fbAuth.currentUser != null) {
            openActivity()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        signInPresenter = SignInPresenterImp(
                FireBaseSignInModel(FirebaseAuth.getInstance()),
                FacebookSignInModel(FirebaseAuth.getInstance()),
                GoogleSignInModel(FirebaseAuth.getInstance()),this)
        signUpBtn.setOnClickListener(this)
        loginButtonIn.setOnClickListener(this)
        gmailBtnIn.setOnClickListener(this)
        signInBtn.setOnClickListener(this)
        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (InternetConnection.isOnline(this) && actionId == EditorInfo.IME_ACTION_DONE)
                signInPresenter.fireBaseSignInAccount(etEmail.text.toString(), etPassword.text.toString())
            else
                Toast.makeText(this, getString(R.string.connection), Toast.LENGTH_SHORT).show()
            true
        }
    }
    private fun facebookHandler() {
        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(getString(R.string.pp), getString(R.string.email)))
        LoginManager.getInstance().registerCallback(mCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        signInPresenter.handleFacebookSignInResults(loginResult.getAccessToken())
                    }

                    override fun onCancel() {
                        Toast.makeText(this@SignInActivity, getString(R.string.facebookCancel), Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(error: FacebookException?) {
                        Toast.makeText(this@SignInActivity, getString(R.string.facebookError), Toast.LENGTH_SHORT).show()
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == rcSignIn) {
            signInPresenter.handleGoogleSignInResults(GoogleSignIn.getSignedInAccountFromIntent(data))
        } else
            mCallbackManager?.onActivityResult(requestCode, resultCode, data)
    }
    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent, rcSignIn)
    }
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.loginButtonIn -> facebookHandler()
            R.id.gmailBtnIn -> signInWithGoogle()
            R.id.signUpBtn -> {
                val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.signInBtn -> {
                if (InternetConnection.isOnline(this)) {
                    signInPresenter.fireBaseSignInAccount(etEmail.text.toString(), etPassword.text.toString())

                }else
                    Toast.makeText(this, getString(R.string.connection), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun openActivity() {
        if (InternetConnection.isOnline(this)) {
            val i = Intent(this@SignInActivity, HomeActivity::class.java)
            startActivity(i)
            finish()
        } else
            Toast.makeText(this, getString(R.string.connection), Toast.LENGTH_SHORT).show()

    }
    override fun onSignInSuccess() {
        openActivity()
    }
    override fun onSignInFailed(errorMsg: Int) {
        Toast.makeText(this, getString(errorMsg), Toast.LENGTH_SHORT).show()
    }
    override fun onSignInFailedWithExeption(e: Exception) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()

    }
    override fun onEmailValidationFail(@StringRes errorMsg: Int) {
        etEmailLayout.error = getString(errorMsg)
    }
    override fun onPasswordValidationFail(@StringRes errorMsg: Int) {
        etPassLayout.error = getString(errorMsg)
    }
    override fun onEmailValidationSuccess() {
        etEmailLayout.isErrorEnabled = false
    }
    override fun onPasswordValidationSuccess() {
        etPassLayout.isErrorEnabled = false
    }
}