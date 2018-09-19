package com.example.rawan.junkfoodtracker

import android.content.Intent

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.View
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


/**
 * Created by rawan on 06/09/18.
 */
class SignUp: AppCompatActivity(), View.OnClickListener {
    var fbAuth=FirebaseAuth.getInstance()
    private val RC_SIGN_IN = 9001
    private var mCallbackManager: CallbackManager? = null
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)
        registationBtn.setOnClickListener(this)
        //facebook handler
        loginButton.setOnClickListener (this)
        gmailBtn.setOnClickListener(this)
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
     private fun facebookHandler() {
        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(getString(R.string.pp), getString(R.string.email)))
        LoginManager.getInstance().registerCallback(mCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken())
                    }

                    override fun onCancel() {
                        Toast.makeText(this@SignUp, getString(R.string.facebookCancel), Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(error: FacebookException?) {
                        Toast.makeText(this@SignUp, getString(R.string.facebookError), Toast.LENGTH_SHORT).show()
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
    private fun signInWithGoogle(){
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
    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        fbAuth.signInWithCredential(credential).addOnCompleteListener{
                        if (!it.isSuccessful)
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this@SignUp, getString(R.string.authentication_issue)+" ${it.exception}", Toast.LENGTH_SHORT).show()
                        else
                            openActivity()
                      }
                }
    fun openActivity(){
        val i= Intent(this@SignUp,HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
        this@SignUp.finish()
    }
    fun validateTitle():Boolean{
        var email=setEmailLayout.editText?.text.toString().trim()
        val ePattern = Patterns.EMAIL_ADDRESS
        val p = java.util.regex.Pattern.compile(ePattern.toString())
        val m = p.matcher(email)
            if(!m.matches()){
                setEmailLayout.error=getString(R.string.invalidEmail)
            return false
            }
//        setEmailLayout.isErrorEnabled=false
        return true
    }
    fun validatePassword():Boolean{
        var pass=setPassLayout.editText?.text.toString().trim()
            if(pass.length<7||pass.isEmpty()){
                setPassLayout.error=getString(R.string.invalidPassword)
            return false
            }
//        setPassLayout.isErrorEnabled=false
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
            R.id.gmailBtn->{
                googleHelper()
                signInWithGoogle()
            }
            R.id.loginButton-> facebookHandler()
            R.id.registationBtn->{
                val email = setEmail.text.toString()
                val password = setPassword.text.toString()
                if (confirm()){
                    fbAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this) {
                        val user = fbAuth.getCurrentUser()
                        val profile= UserProfileChangeRequest.Builder().setDisplayName(email.removeRange(email.indexOf("@"),email.length)).build()
                        user?.updateProfile(profile)
                        if (it.isSuccessful){
                            Toast.makeText(this, getString(R.string.welcome), Toast.LENGTH_SHORT).show()
                            openActivity()
                        }
                        else
                            Toast.makeText(this, "${it.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}