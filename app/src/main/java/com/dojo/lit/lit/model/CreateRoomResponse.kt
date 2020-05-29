package com.dojo.lit.lit.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreateRoomResponse (
    @SerializedName("gameid")
    val gameId: String
)  : Parcelable