package com.dojo.lit.lit.util

import com.dojo.lit.R

object SetNamesUtil {
    private val map: MutableMap<String, Int> = HashMap()
    init {
        map.put(SetApiNames.Sl, R.string.spades_lower)
        map.put(SetApiNames.Sh, R.string.spades_higher)
        map.put(SetApiNames.Hl, R.string.hearts_lower)
        map.put(SetApiNames.Hh, R.string.hearts_higher)
        map.put(SetApiNames.Dl, R.string.diamonds_lower)
        map.put(SetApiNames.Dh, R.string.diamonds_higher)
        map.put(SetApiNames.Cl, R.string.clubs_lower)
        map.put(SetApiNames.Ch, R.string.clubs_higher)
        map.put(SetApiNames.Ch, R.string.jokers)
    }

    fun getMapping(): Map<String, Int>{
        return map
    }
}
