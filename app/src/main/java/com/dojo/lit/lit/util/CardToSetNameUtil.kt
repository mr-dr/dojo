package com.dojo.lit.lit.util

import com.dojo.lit.util.CardApiNames

object CardToSetNameUtil {
    private val map: MutableMap<String, String> = HashMap()
    init {
        map.put(CardApiNames.S2, SetApiNames.Sl)
        map.put(CardApiNames.S3, SetApiNames.Sl)
        map.put(CardApiNames.S4, SetApiNames.Sl)
        map.put(CardApiNames.S5, SetApiNames.Sl)
        map.put(CardApiNames.S6, SetApiNames.Sl)
        map.put(CardApiNames.S7, SetApiNames.Sl)

        map.put(CardApiNames.S9, SetApiNames.Sh)
        map.put(CardApiNames.S10, SetApiNames.Sh)
        map.put(CardApiNames.Sj, SetApiNames.Sh)
        map.put(CardApiNames.Sq, SetApiNames.Sh)
        map.put(CardApiNames.Sk, SetApiNames.Sh)
        map.put(CardApiNames.Sa, SetApiNames.Sh)

        map.put(CardApiNames.C2, SetApiNames.Cl)
        map.put(CardApiNames.C3, SetApiNames.Cl)
        map.put(CardApiNames.C4, SetApiNames.Cl)
        map.put(CardApiNames.C5, SetApiNames.Cl)
        map.put(CardApiNames.C6, SetApiNames.Cl)
        map.put(CardApiNames.C7, SetApiNames.Cl)

        map.put(CardApiNames.C9, SetApiNames.Ch)
        map.put(CardApiNames.C10, SetApiNames.Ch)
        map.put(CardApiNames.Cj, SetApiNames.Ch)
        map.put(CardApiNames.Cq, SetApiNames.Ch)
        map.put(CardApiNames.Ck, SetApiNames.Ch)
        map.put(CardApiNames.Ca, SetApiNames.Ch)

        map.put(CardApiNames.D2, SetApiNames.Dl)
        map.put(CardApiNames.D3, SetApiNames.Dl)
        map.put(CardApiNames.D4, SetApiNames.Dl)
        map.put(CardApiNames.D5, SetApiNames.Dl)
        map.put(CardApiNames.D6, SetApiNames.Dl)
        map.put(CardApiNames.D7, SetApiNames.Dl)

        map.put(CardApiNames.D9, SetApiNames.Dh)
        map.put(CardApiNames.D10, SetApiNames.Dh)
        map.put(CardApiNames.Dj, SetApiNames.Dh)
        map.put(CardApiNames.Dq, SetApiNames.Dh)
        map.put(CardApiNames.Dk, SetApiNames.Dh)
        map.put(CardApiNames.Da, SetApiNames.Dh)

        map.put(CardApiNames.H2, SetApiNames.Hl)
        map.put(CardApiNames.H3, SetApiNames.Hl)
        map.put(CardApiNames.H4, SetApiNames.Hl)
        map.put(CardApiNames.H5, SetApiNames.Hl)
        map.put(CardApiNames.H6, SetApiNames.Hl)
        map.put(CardApiNames.H7, SetApiNames.Hl)

        map.put(CardApiNames.H9, SetApiNames.Hh)
        map.put(CardApiNames.H10, SetApiNames.Hh)
        map.put(CardApiNames.Hj, SetApiNames.Hh)
        map.put(CardApiNames.Hq, SetApiNames.Hh)
        map.put(CardApiNames.Hk, SetApiNames.Hh)
        map.put(CardApiNames.Ha, SetApiNames.Hh)

        map.put(CardApiNames.Jb, SetApiNames.j)
        map.put(CardApiNames.Jr, SetApiNames.j)
        map.put(CardApiNames.S8, SetApiNames.j)
        map.put(CardApiNames.C8, SetApiNames.j)
        map.put(CardApiNames.D8, SetApiNames.j)
        map.put(CardApiNames.H8, SetApiNames.j)
    }

    fun getMapping(): Map<String, String>{
        return map
    }
}