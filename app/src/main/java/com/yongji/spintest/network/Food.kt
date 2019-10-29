package com.yongji.spintest.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Food(
    @Json(name = "url") val url: String,
    @Json(name = "title") val title: String) : Parcelable