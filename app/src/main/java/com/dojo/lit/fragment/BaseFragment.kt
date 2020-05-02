package com.dojo.lit.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.dojo.lit.base.BaseActivity

abstract open class BaseFragment : Fragment() {
    final fun sendFirebaseEvent(event: String, bundle: Bundle) {
        (activity as BaseActivity).sendFirebaseEvent(event, bundle)
    }

    fun <T : View> findViewById(id: Int): T {
        if (view == null) {
            throw NullPointerException("no view attached to this fragment")
        }
        return view!!.findViewById(id)
    }
}
