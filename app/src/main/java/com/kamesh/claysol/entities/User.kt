package com.kamesh.claysol.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    val id: String = "",
    val username:String = "",
    val email: String="",
    val image: String = "",
    val mobile: Long= 0,
    val address:String = "",
    val city: String = "",
    val state: String ="",
    val pinCode: Long= 0,
    val profileCompleted: Int = 0
): Parcelable
