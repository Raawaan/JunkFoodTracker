package com.example.rawan.junkfoodtracker

import android.content.Intent

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.sign_up.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.util.*
import com.google.firebase.auth.FacebookAuthProvider
import com.facebook.AccessToken
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_main.*
import com.example.rawan.junkfoodtracker.R.string.email




/**
 * Created by rawan on 06/09/18.
 */
class SignUp: AppCompatActivity() {
    var fbAuth=FirebaseAuth.getInstance()
    private val RC_SIGN_IN = 9001
    private var mCallbackManager: CallbackManager? = null
    lateinit private var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)
        registationBtn.setOnClickListener {
            val email = setEmail.text.toString()
            val password = setPassword.text.toString()
            if (confirm()){
                fbAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this) {
                    val user = fbAuth.getCurrentUser()
                    val profile= UserProfileChangeRequest.Builder().setDisplayName(email.removeRange(email.indexOf("@"),email.length)).build()
                    user?.updateProfile(profile)
                    if (it.isSuccessful){
                        Toast.makeText(this, "Just Registered", Toast.LENGTH_SHORT).show()
                    openActivity()
                    }
                    else
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //facebook handler
        loginButton.setOnClickListener {
            facebookHandler()
        }
        //gmail handler
        gmailBtn.setOnClickListener {
            googleHelper()
            signInWithGoogle()
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
     fun facebookHandler() {
        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance().registerCallback(mCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken())
                    }

                    override fun onCancel() {
                        Toast.makeText(this@SignUp, "facebook cancel", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(error: FacebookException?) {
                        Toast.makeText(this@SignUp, "facebook error", Toast.LENGTH_SHORT).show()
                    }
                })
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
    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        fbAuth.signInWithCredential(credential).addOnCompleteListener{
                        if (!it.isSuccessful)
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this@SignUp, "Authentication failed ${it.exception}", Toast.LENGTH_SHORT).show()
                        else
                            openActivity()
                      }
                }
    fun openActivity(){
        val i= Intent(this@SignUp,Home::class.java)
        startActivity(i)
        finish()
    }

    fun validateTitle():Boolean{
        var email=setEmailLayout.editText?.text.toString().trim()
        val ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
        val p = java.util.regex.Pattern.compile(ePattern)
        val m = p.matcher(email)
            if(!m.matches()){
                setEmailLayout.error="Invalid Email"
            return false
            }
        setEmailLayout.isErrorEnabled=false
        return true
    }
    fun validatePassword():Boolean{
        var pass=setPassLayout.editText?.text.toString().trim()
            if(pass.length<7||pass.isEmpty()){
                setPassLayout.error="Short Password"
            return false
            }
        setPassLayout.isErrorEnabled=false
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