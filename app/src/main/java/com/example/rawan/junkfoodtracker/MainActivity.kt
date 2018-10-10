package com.example.rawan.junkfoodtracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.rawan.junkfoodtracker.SignUp.View.SignUpActivity
import com.facebook.AccessToken
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
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val RC_SIGN_IN = 9001
    private var mCallbackManager: CallbackManager? = null
    lateinit private var mGoogleSignInClient: GoogleSignInClient
    var fbAuth= FirebaseAuth.getInstance()
    override fun onStart() {
        super.onStart()
        if (fbAuth.getCurrentUser()!=null){
            openActivity()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        signUpBtn.setOnClickListener(this)
        loginButtonIn.setOnClickListener(this)
        gmailBtnIn.setOnClickListener(this)
        signInBtn.setOnClickListener(this)
        etPassword.setOnEditorActionListener { v, actionId, event ->
            if (InternetConnection.isOnline(this)){
                if (confirm()) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        firebaseSignIn()
                    }}
                else
                    Toast.makeText(this, getString(R.string.connection), Toast.LENGTH_SHORT).show()
            }
            true
        }
    }
    fun facebookHandler() {
        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(getString(R.string.pp), getString(R.string.email)))
        LoginManager.getInstance().registerCallback(mCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken())
                    }
                    override fun onCancel() {
                        Toast.makeText(this@MainActivity, getString(R.string.facebookCancel), Toast.LENGTH_SHORT).show()
                    }
                    override fun onError(error: FacebookException?) {
                        Toast.makeText(this@MainActivity, getString(R.string.facebookError), Toast.LENGTH_SHORT).show()
                    }
                })
    }
    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        fbAuth.signInWithCredential(credential).addOnCompleteListener{
            if (!it.isSuccessful)
            // If sign in fails, display a message to the user.
                Toast.makeText(this@MainActivity, getString(R.string.authentication_issue)+"${it.exception}", Toast.LENGTH_SHORT).show()
            else
                openActivity()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==RC_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
        else
            mCallbackManager?.onActivityResult(requestCode, resultCode, data)
    }
    private fun googleHelper() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }
    fun signInWithGoogle(){
        val intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent,RC_SIGN_IN)
    }
    fun handleResult(completedTask : Task<GoogleSignInAccount>){
        try{
            val account= completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        }catch (e:Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        fbAuth.signInWithCredential(credential).addOnCompleteListener{
            if (!it.isSuccessful)
                Toast.makeText(this, getString(R.string.authentication_issue)+" ${it.exception} ", Toast.LENGTH_SHORT).show()
            else
                openActivity()
        }
    }
    fun validateTitle():Boolean{
        val email=etEmailLayout.editText?.text.toString().trim()
        val ePattern = Patterns.EMAIL_ADDRESS
        val p = java.util.regex.Pattern.compile(ePattern.toString())
        val m = p.matcher(email)
        if(!m.matches()){
            etEmailLayout.error=getString(R.string.invalidEmail)
            return false
        }
        return true
    }
    fun validatePassword():Boolean{
        val pass=etPassLayout.editText?.text.toString().trim()
        if(pass.length<7||pass.isEmpty()){
            etPassLayout.error=getString(R.string.invalidPassword)
            return false
        }
//        etPassLayout.isErrorEnabled = false
        return true
    }
   private fun confirm():Boolean{
        if (validateTitle()){
            if(validatePassword())
                return true
        }
        return false
    }
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.loginButtonIn->facebookHandler()
            R.id.gmailBtnIn->{
                googleHelper()
                signInWithGoogle()
            }
            R.id.signUpBtn->{ val intent=Intent(this@MainActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.signInBtn->{
                if (InternetConnection.isOnline(this))
                if (confirm()){
                    firebaseSignIn()
                }
                else
                    Toast.makeText(this, getString(R.string.connection), Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun firebaseSignIn() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful)
                openActivity()
            else
                Toast.makeText(this, getString(R.string.invalidEmailPass), Toast.LENGTH_SHORT).show()
        }
    }

    private fun openActivity(){
        if(InternetConnection.isOnline(this)){
        val i= Intent(this@MainActivity,HomeActivity::class.java)
        startActivity(i)
        finish()
        }
        else
            Toast.makeText(this, getString(R.string.connection), Toast.LENGTH_SHORT).show()

    }
}