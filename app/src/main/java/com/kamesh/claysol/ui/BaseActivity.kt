package com.kamesh.claysol.ui

import android.app.Dialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.kamesh.claysol.R

open class BaseActivity : AppCompatActivity() {
    private lateinit var dialog: Dialog
    private var doubleBackToExitPressedOnce = false

    fun showErrorMessage( message: String, errorMessage: Boolean){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        val  snackBarView = snackBar.view

        if (errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.error_color
                )
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.successColor
                )
            )
        }
        snackBar.show()
    }

    fun showProgressBar(){

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
    fun hideProgressBar() {

        dialog.dismiss()
    }

    fun doubleBackToExit(){
        if (doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this,
            "Please click back again to exit",
            Toast.LENGTH_SHORT
        ).show()
        @Suppress("DEPRECATION")
        android.os.Handler().postDelayed({doubleBackToExitPressedOnce = false},2000)
    }
}