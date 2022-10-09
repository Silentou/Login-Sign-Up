package com.kamesh.claysol.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.TextUtils
import android.transition.TransitionManager
import android.util.Log
import android.util.Patterns
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kamesh.claysol.R
import com.kamesh.claysol.constant.Constants
import com.kamesh.claysol.databinding.ActivityRegisterBinding
import com.kamesh.claysol.entities.User
import com.kamesh.claysol.firestore.FirestoreClass

class RegisterActivity : BaseActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        @Suppress("DEPRECATION")
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(R.color.black_overlay)

        auth = Firebase.auth

        binding.userRegisterButton.setOnClickListener{
            registerUser()
        }
        binding.registerBackBtn.setOnClickListener {
            onBackPressed()

        }
    }

    //TO register User in firestore Database
    private fun registerUser() {

        if (validateRegisterUser()){

            showProgressBar()

            val email: String  = binding.rEmailInputTextField.text.toString().trim{it <= ' '}
            val password: String  = binding.cPasswordInputTextField.text.toString().trim{it <= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task->
                    hideProgressBar()
                    if (task.isSuccessful) {
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnSuccessListener {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                val user = User(
                                    firebaseUser.uid,
                                    binding.usernameInputTextField.text.toString(),
                                    binding.rEmailInputTextField.text.toString().trim{it <= ' '}

                                )

                                FirestoreClass().registerUser(this,user)
                                intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
                                Toast.makeText(this,"Please Verify Your Email", Toast.LENGTH_LONG).show()
                                finish()
                            }
                            ?.addOnFailureListener {
                                showErrorMessage("Error occurred $it", true)
                            }
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        showErrorMessage("Registration Failed", true )

                    }

                }
                .addOnFailureListener {
                    showErrorMessage("error Occurred ${it.localizedMessage}", true)
                }
        }
    }

    //to Validate The given Information
    private fun validateRegisterUser(): Boolean {
        return when{
            TextUtils.isEmpty(binding.usernameInputTextField.text.toString()) -> {
                showErrorMessage("Please Enter your Username",true)
                false
            }
            TextUtils.isEmpty(binding.rEmailInputTextField.text.toString().trim{it <= ' '})
                    || !Patterns.EMAIL_ADDRESS.matcher(binding.rEmailInputTextField.text.toString()).matches()
            -> {
                showErrorMessage("Please Enter your Valid Email Address",true)
                false
            }
            TextUtils.isEmpty(binding.passwordInputTextField.text.toString().trim{it <= ' '}) || binding.passwordInputTextField.length() <= 7
                    || !binding.passwordInputTextField.text.toString().matches(".*[A-Z].*".toRegex())
                    || !binding.passwordInputTextField.text.toString().matches(".*[0-9]*.".toRegex())
                    || !binding.passwordInputTextField.text.toString().matches(".*[!@#$%^&*()_+=-]*.".toRegex())
            -> {
                showErrorMessage("Password Should contain 8 Characters, One Capital Letter, One Number and Special Character ",true)
                false
            }

            TextUtils.isEmpty(binding.cPasswordInputTextField.text.toString().trim{it <= ' '})  -> {
                showErrorMessage("Please Enter your Confirm Password",true)
                false
            }

            binding.passwordInputTextField.text.toString().trim{it <= ' '} != binding.cPasswordInputTextField.text.toString().trim{it <= ' '} ->{
                showErrorMessage("Password Mismatch",true)
                false
            }

            !binding.checkTermsRegister.isChecked ->{
                showErrorMessage("Please Agree The Terms & Conditions and Privacy Policy",true)
                false
            }

            else -> {
//                showErrorMessage("Your Details are Saved", false)
                true
            }
        }
    }

    fun userRegistrationSuccess(){
        hideProgressBar()
        Toast.makeText(this,"Registered Successfully", Toast.LENGTH_SHORT).show()
    }

}