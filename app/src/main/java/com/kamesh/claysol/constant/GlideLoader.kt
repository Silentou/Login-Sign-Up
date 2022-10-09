package com.kamesh.claysol.constant

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kamesh.claysol.R
import java.io.IOException

class GlideLoader(val context: Context) {
    fun loadUserPicture(image: Any,imageView: ImageView){
        try{
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.user)
                .into(imageView)
        }catch (e: IOException){
            e.printStackTrace()
        }
    }
}