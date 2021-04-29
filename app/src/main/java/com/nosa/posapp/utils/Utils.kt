package com.nosa.posapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import com.nosa.posapp.db.CachedUser
import java.util.*


class Utils {
    companion object{
        fun setLocale(activity: Activity, languageCode: String?) {
            Log.d("Utils", "--- lang= ${languageCode}")
            if (languageCode == null) {
                var cachedUser = CachedUser(activity)
                var user = cachedUser.getUser()
                user?.let { user ->  user.lang = "en"}
                cachedUser.saveUser(user!!)
                val locale = Locale("en")
                Locale.setDefault(locale)
                val resources: Resources = activity.resources
                val config: Configuration = resources.getConfiguration()
                config.setLocale(locale)
                resources.updateConfiguration(config, resources.getDisplayMetrics())
            }else {
                val locale = Locale(languageCode)
                Locale.setDefault(locale)
                val resources: Resources = activity.resources
                val config: Configuration = resources.getConfiguration()
                config.setLocale(locale)
                resources.updateConfiguration(config, resources.getDisplayMetrics())
            }
        }

        fun openLinkInBrowser(data: String, context: Context){
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data))
            val title = "Complete Action Using"
            val chooser = Intent.createChooser(intent, title)
            context.startActivity(chooser)
        }

        /**
         * Returns the unique identifier for the device
         *
         * @return unique identifier for the device
         */
        fun getDeviceIMEI(context: Context): String? {
            val deviceId: String

            deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            } else {
                val mTelephony =
                    context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) !== PackageManager.PERMISSION_GRANTED) {
                        return ""
                    }
                }
                assert(mTelephony != null)
                if (mTelephony!!.deviceId != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        mTelephony!!.imei
                    } else {
                        mTelephony!!.deviceId
                    }
                } else {
                    Settings.Secure.getString(
                        context.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                }
            }
            Log.d("deviceId", deviceId)
            return deviceId
        }
    }

    /**
     * Returns the unique identifier for the device
     *
     * @return unique identifier for the device
     */
    fun getDeviceIMEI(context: Context): String? {
        var deviceUniqueIdentifier: String? = null
        val tm =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        if (null != tm) {
            try {
                deviceUniqueIdentifier = tm.deviceId
            }catch (ex: Exception){}
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length) {
            deviceUniqueIdentifier =
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)
        }
        return "12345678"
    }

}