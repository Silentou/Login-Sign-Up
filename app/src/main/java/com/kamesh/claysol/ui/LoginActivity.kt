package com.kamesh.claysol.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kamesh.claysol.R
import com.kamesh.claysol.constant.Constants
import com.kamesh.claysol.databinding.ActivityLoginBinding
import com.kamesh.claysol.entities.User
import com.kamesh.claysol.firestore.FirestoreClass

@Suppress("DEPRECATION")
class  LoginActivity : BaseActivity(),View.OnClickListener{

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        @Suppress("DEPRECATION")
        supportActionBar?.hide()

        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(R.color.black_overlay)

        auth = Firebase.auth
        checkUser()
        //  button links
        binding.fgtPassword.setOnClickListener(this@LoginActivity)
        binding.loginBtn.setOnClickListener(this@LoginActivity)
        binding.registerLink.setOnClickListener(this@LoginActivity)


    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
            super.onConfigurationChanged(newConfig)
    }

    private fun loginRegisterUser() {
        if (validateLoginDetails()){
            showProgressBar()
            val lEmail= binding.lEmailInputTextField.text.toString().trim{it <= ' '}
            val lPassword = binding.lPassInputTextField.text.toString().trim{it <= ' '}

            auth.signInWithEmailAndPassword(lEmail,lPassword)
                .addOnCompleteListener(this) { task ->
                    hideProgressBar()
                    if (task.isSuccessful) {
                        val verification = auth.currentUser?.isEmailVerified
                        if (verification == true){
                            Toast.makeText(this,"Welcome", Toast.LENGTH_LONG).show()
                            FirestoreClass().getUserDetails(this@LoginActivity)
                        }else{
                            showErrorMessage("Email is not registered or verified ",true)
                        }
                    } else {
                        showErrorMessage( "Authentication failed ${task.exception}.",
                            true)
                    }
                }
                .addOnFailureListener {
                    showErrorMessage("Login Failed ${it.localizedMessage}", true)
                }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when{
            TextUtils.isEmpty(binding.lEmailInputTextField.text.toString().trim{it <= ' '})  -> {
                showErrorMessage("Please Enter Your Email",true)
                false
            }
            TextUtils.isEmpty(binding.lPassInputTextField.text.toString().trim{it <= ' '}) -> {
                showErrorMessage("Please Enter Your Valid Password",true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun userLoggedInService(user: User) {
        hideProgressBar()
        if (user.profileCompleted == 0){
            val intent =Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }
        else{
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        finish()
    }

    override fun onClick(view: View?) {
        if (view!=null){
            when(view.id){
                R.id.fgtPassword ->{
                    val intent = Intent(this@LoginActivity, VerifyActivity::class.java)
                    startActivity(intent)
                }
                R.id.loginBtn ->{
                    loginRegisterUser()
                }
                R.id.registerLink ->{
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}