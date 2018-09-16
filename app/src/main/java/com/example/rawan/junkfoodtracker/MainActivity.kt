package com.example.rawan.junkfoodtracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.view.View
import android.widget.Toast
import com.example.rawan.junkfoodtracker.R.id.setEmail
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.sign_up.*
import java.util.*


class MainActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_main)
        signUpBtn.setOnClickListener {
            val intent=Intent(this@MainActivity,SignUp::class.java)
            startActivity(intent)
            finish()
        }
        loginButtonIn.setOnClickListener {
            facebookHandler()
        }
        gmailBtnIn.setOnClickListener {
            googleHelper()
            signInWithGoogle()
        }
        signInBtn.setOnClickListener{
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (confirm()){
            fbAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) {
                if (it.isSuccessful)
                    openActivity()
                else
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
            }
        }
    }
    fun openActivity(){
        val i= Intent(this@MainActivity,Home::class.java)
        startActivity(i)
        finish()
    }
    fun facebookHandler() {
        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance().registerCallback(mCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken())
                    }
                    override fun onCancel() {
                        Toast.makeText(this@MainActivity, "facebook cancel", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(error: FacebookException?) {
                        Toast.makeText(this@MainActivity, "facebook error", Toast.LENGTH_SHORT).show()
                    }
                })
    }
    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        fbAuth.signInWithCredential(credential).addOnCompleteListener{
            if (!it.isSuccessful)
            // If sign in fails, display a message to the user.
                Toast.makeText(this@MainActivity, "Authentication failed ${it.exception}", Toast.LENGTH_SHORT).show()
            else
                openActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
            Toast.makeText(this, "Authentication Failed ${it.exception} ", Toast.LENGTH_SHORT).show()
        else
            openActivity()
    }
    }

    fun validateTitle():Boolean{
        var email=etEmailLayout.editText?.text.toString().trim()
        val ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
        val p = java.util.regex.Pattern.compile(ePattern)
        val m = p.matcher(email)
        if(!m.matches()){
            etEmailLayout.error="Invalid Email"
            return false
        }
        etEmailLayout.isErrorEnabled = false
        return true
    }
    fun validatePassword():Boolean{
        var pass=etPassLayout.editText?.text.toString().trim()
        if(pass.length<7||pass.isEmpty()){
            etPassLayout.error="Short Password"
            return false
        }
        etPassLayout.isErrorEnabled = false
        return true
    }
    fun confirm():Boolean{
        if (validateTitle()){
            if(validatePassword())
                return true
        }
        return false
    }
}