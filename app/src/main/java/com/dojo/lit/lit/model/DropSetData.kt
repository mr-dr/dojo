package com.dojo.lit.lit.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DropSetData(
    val playerA: Int,
    val playerB: Int,
    val playerC: Int,
    val cardsA: List<String>,
    val cardsB: List<String>,
    val cardsC: List<String>
): Parcelable