package com.example.rawan.junkfoodtracker.SignUp.View

import android.content.Intent

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.rawan.junkfoodtracker.InternetConnection
import com.example.rawan.junkfoodtracker.OnBoarding
import com.example.rawan.junkfoodtracker.R
import com.example.rawan.junkfoodtracker.SignUp.Model.FacebookSignUpModel
import com.example.rawan.junkfoodtracker.SignUp.Model.FireBaseSignUpModel
import com.example.rawan.junkfoodtracker.SignUp.Model.GoogleSignUpModel
import com.example.rawan.junkfoodtracker.SignUp.Presenter.SignUpPresenter
import com.example.rawan.junkfoodtracker.SignUp.Presenter.SignUpPresenterImp
import com.example.rawan.junkfoodtracker.SignUp.Presenter.isValidEmail
import com.example.rawan.junkfoodtracker.SignUp.Presenter.isValidPassword
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.sign_up.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.util.*
import com.facebook.AccessToken


/**
 * Created by rawan on 06/09/18.
 */
class SignUpActivity : AppCompatActivity(), SignUpView, View.OnClickListener {
    lateinit var signUpPresenter: SignUpPresenter
    private val RC_SIGN_IN = 9001
    private var mCallbackManager: CallbackManager? = null
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)
        signUpPresenter = SignUpPresenterImp(FireBaseSignUpModel(FirebaseAuth.getInstance()),
                GoogleSignUpModel(FirebaseAuth.getInstance()),
                FacebookSignUpModel(FirebaseAuth.getInstance()),
                this)
        registationBtn.setOnClickListener(this)
        loginButton.setOnClickListener(this)
        gmailBtn.setOnClickListener(this)
        setEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                 signUpPresenter.validateEmail(editable.toString())
                if (!setEmailLayout.editText?.text.toString().trim().isValidEmail())
                    setEmailLayout.error = getString(R.string.invalidEmail)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        setPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                signUpPresenter.validatePassword(editable.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        setPassword.setOnEditorActionListener { _, actionId, _ ->
            if (InternetConnection.isOnline(this) && actionId == EditorInfo.IME_ACTION_DONE)
                signUpPresenter.createFireBaseAccount(setEmail.text.toString(), setPassword.text.toString())
            else
                Toast.makeText(this, getString(R.string.connection), Toast.LENGTH_SHORT).show()
            return@setOnEditorActionListener true
        }
    }
    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            signUpPresenter.handleGoogleSignUpResults(task)
        } else
            mCallbackManager?.onActivityResult(requestCode, resultCode, data)
    }//todo be handled
    private fun facebookHandler() {
        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"))
        LoginManager.getInstance().registerCallback(mCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        handleFacebookAccessToken(loginResult.accessToken)
                    }
                    override fun onCancel() {
                        Toast.makeText(this@SignUpActivity, getString(R.string.facebookCancel), Toast.LENGTH_SHORT).show()
                    }
                    override fun onError(error: FacebookException?) {
                        Toast.makeText(this@SignUpActivity, getString(R.string.facebookError), Toast.LENGTH_SHORT).show()
                    }
                })
    }
    private fun handleFacebookAccessToken(token: AccessToken) = signUpPresenter.handleFacebookSignUpResults(token)
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.gmailBtn -> signInWithGoogle()
            R.id.loginButton -> facebookHandler()
            R.id.registationBtn -> {
                if (InternetConnection.isOnline(this)) {
                    signUpPresenter.createFireBaseAccount(setEmail.text.toString(), setPassword.text.toString())
                } else
                    Toast.makeText(this, getString(R.string.connection), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun openActivity() {
        val i = Intent(this@SignUpActivity, OnBoarding::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
    override fun onSignUpSuccess() {
        Toast.makeText(this, getString(R.string.welcome), Toast.LENGTH_SHORT).show()
        openActivity()
    }
    override fun onSignUpFailed(e: Exception) {
        Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
    }
    override fun onEmailValidationFail(@StringRes errorMsg: Int) {
        setEmailLayout.error = getString(errorMsg)
    }
    override fun onPasswordValidationFail(@StringRes errorMsg: Int) {
        setPassLayout.error = getString(errorMsg)
    }
    override fun onEmailValidationSuccess() {
        setEmailLayout.isErrorEnabled = false
    }
    override fun onPasswordValidationSuccess() {
        setPassLayout.isErrorEnabled = false
    }
}