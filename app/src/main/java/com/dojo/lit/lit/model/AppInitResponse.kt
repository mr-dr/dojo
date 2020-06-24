package com.dojo.lit.lit.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppInitResponse(
    @SerializedName("force_upgrade")
    val forceUpgrade: Boolean
): Parcelable