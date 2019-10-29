package com.yongji.spintest.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FoodApiResponse (
    @field:Json(name = "data") var data: List<Food>
) : Parcelable