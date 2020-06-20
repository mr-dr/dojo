package com.dojo.lit

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.net.ConnectivityManager
import android.widget.Toast
import android.media.RingtoneManager
import android.media.Ringtone
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import com.dojo.lit.util.SharedPrefManager
import com.dojo.lit.util.TextUtil
import java.util.*


object Utils {

    val DEVICE_ID_NOT_FOUND = "didnf"

    fun makeToastLong(messageId: Int) {
        val message = AppController.getInstance().getResources().getString(messageId)
        val toast = Toast.makeText(AppController.getApplicationContext(), message, Toast.LENGTH_LONG)
        toast.show()
    }

    fun makeToastLong(message: String) {
        val toast = Toast.makeText(AppController.getApplicationContext(), message, Toast.LENGTH_LONG)
        toast.show()
    }

    fun makeNotificationSound() {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(AppController.getApplicationContext(), notification)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getAppVersionCodeFromPackage(): Int {
        try{
            val application = AppController.getInstance();
            val packageInfo: PackageInfo = application.packageManager.
                getPackageInfo(application.packageName, 0)
            return packageInfo.versionCode
        } catch (exception: Exception) {
            if (BuildConfig.DEBUG) {
                Log.d("AppVersion error", exception.toString())
            }
            return -1
        }
    }

    fun getUniqueDeviceId(): String {
        //0 return persistence saved value.
        var deviceId = SharedPrefManager.readSharedPrefString(SharedPrefManager.DEVICE_ID_KEY, "")

        if (TextUtil.isEmpty(deviceId)) {
            //1 check telephony manager
            deviceId = getDeviceImei()

            if (TextUtil.isEmpty(deviceId)) {
                //2. Setting id
                deviceId = getAndroidId()
                if (TextUtil.isEmpty(deviceId)) {
                    //3. random id
                    deviceId = UUID.randomUUID().toString()
                }
            }
            if (TextUtil.isEmpty(deviceId)) {
                Log.e("core", "DojoApp - Getting empty device id.")
            }
            if (deviceId != null) {
                SharedPrefManager.writeSharedPrefString(SharedPrefManager.DEVICE_ID_KEY, deviceId)
            }
        }

        return deviceId ?: DEVICE_ID_NOT_FOUND
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceImei(): String? {
        var deviceId: String? = null
        val tm =
            AppController.getInstance().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {
            deviceId = tm.deviceId
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }

        return deviceId
    }

    private fun getAndroidId(): String {
        var id = SharedPrefManager.readSharedPrefString(SharedPrefManager.ANDROID_ID_KEY, "")
        if (TextUtil.isEmpty(id)) {
            id = Settings.Secure.getString(
                AppController.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID
            )
            SharedPrefManager.writeSharedPrefString(SharedPrefManager.ANDROID_ID_KEY, id)
        }
        return id ?: ""
    }


    fun getColor(id:Int): Int {
        return AppController.getResources().getColor(id)
    }

    fun getDimen(id:Int): Float {
        return AppController.getResources().getDimension(id)
    }

    fun getString(id:Int): String {
        return AppController.getResources().getString(id)
    }

    fun getString(id: Int, vararg strings: String): String {
        return AppController.getResources().getString(id, strings)
    }

    fun isNetworkAvailable(): Boolean {
        val context = AppController.getInstance()
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

}