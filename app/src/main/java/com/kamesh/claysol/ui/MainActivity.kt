package com.kamesh.claysol.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kamesh.claysol.R
import com.kamesh.claysol.constant.Constants
import com.kamesh.claysol.constant.GlideLoader
import com.kamesh.claysol.databinding.ActivityMainBinding
import com.kamesh.claysol.entities.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(),View.OnClickListener {
    lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserDetails: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(R.color.black_overlay)

        checkedUser()

        binding.topLayer.cardElevation = 0F
        binding.logout.setOnClickListener(this@MainActivity)
        binding.icForwardAccount5.setOnClickListener(this@MainActivity)
        binding.icLogout.setOnClickListener(this@MainActivity)
        binding.account1.setOnClickListener(this@MainActivity)

    }

    private fun checkedUser(){
        auth = FirebaseAuth.getInstance()
        checkUser()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.logout -> {
                    logoutFun()
                }
                R.id.ic_forwardAccount5 -> {
                    logoutFun()
                }
                R.id.ic_logout -> {
                    logoutFun()
                }
                R.id.account_1 -> {
                    val intent = Intent(this@MainActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }
            }
        }
    }

    private fun logoutFun() {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    private fun getUserDetails() {
            showProgressBar()
            userFunctional(this)
        }

    private fun userFunctional(activity: Activity) {
        hideProgressBar()
        val rootRef = FirebaseFirestore.getInstance().collection(Constants.USERS)
        val userRef = rootRef.document(getCurrentUserIDs()).get()
        userRef.addOnSuccessListener { document ->

            val user = document.toObject(User::class.java)!!
            when (activity) {
                is MainActivity -> {
                    activity.userDetailsSuccess(user)
                }
            }
        }
            .addOnFailureListener {
                when (activity) {
                    is MainActivity -> {
                        activity.hideProgressBar()
                    }
                }
            }
        }

    override fun onResume() {
        super.onResume()
        getUserDetails()

    }

    @SuppressLint("SetTextI18n")
    private fun userDetailsSuccess(user: User) {
            mUserDetails = user
            hideProgressBar()
            GlideLoader(this@MainActivity).loadUserPicture(user.image, profileSettingImage)
            profileUsername.text = user.username
            emailSettings.text = user.email
            phoneSettings.text = "${user.mobile}"
            addressSettings.text = "${user.address} ${user.city} ${user.state} ${user.pinCode}"
        }

    private fun getCurrentUserIDs(): String {
            val currentUser = FirebaseAuth.getInstance().currentUser
            var currentUserID = ""
            if (currentUser != null) {
                currentUserID = currentUser.uid
            }
            return currentUserID
        }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if (firebaseUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }

}
