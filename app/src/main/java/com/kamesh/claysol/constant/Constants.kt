package com.kamesh.claysol.constant

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS: String ="users"
    const val MY_PREFERENCES: String ="MYPrefs"
    const val LOGGED_IN_USERNAME: String ="logged_in_username"
    const val LOGGED_IN_IMAGE: String = "logged_in_image"
    const val EXTRA_USER_DETAILS: String ="extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val MOBILE: String = "mobile"
    const val ADDRESS: String = "address"
    const val CITY: String = "city"
    const val STATE: String = "state"
    const val PIN_CODE: String = "pinCode"
    const val IMAGE:String = "image"
    const val PROFILE_COMPLETED:String = "profileCompleted"
    const val USER_PROFILE_IMAGE: String = "User_profile_image"
    fun showImageChooser(activity: Activity){
        val  galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent,PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?):String?{
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}