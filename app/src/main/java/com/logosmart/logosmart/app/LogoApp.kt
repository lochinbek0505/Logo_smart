package com.azamovhudstc.logosmart.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
@SuppressLint("StaticFieldLeak")
class LogoApp : MultiDexApplication() {

    init {
        instance = this
    }

    companion object {
        const val SOZO_NOTIFICATIONS_CHANNEL_ID = "SOZO_NOTIFICATIONS_CHANNEL_ID"
        const val TAG_PERIODIC_WORK_REQUEST = "periodic_work_request"
        var instance: LogoApp? = null
        var context: Context? = null
        fun currentContext(): Context? {
            return instance?.mFTActivityLifecycleCallbacks?.currentActivity ?: context
        }

        fun currentActivity(): Activity? {
            return instance?.mFTActivityLifecycleCallbacks?.currentActivity
        }
    }

    val mFTActivityLifecycleCallbacks = FTActivityLifecycleCallbacks()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    inner class FTActivityLifecycleCallbacks : ActivityLifecycleCallbacks {
        var currentActivity: Activity? = null
        override fun onActivityCreated(p0: Activity, p1: Bundle?) {}
        override fun onActivityStarted(p0: Activity) {
            currentActivity = p0
        }

        override fun onActivityResumed(p0: Activity) {
            currentActivity = p0
        }

        override fun onActivityPaused(p0: Activity) {}
        override fun onActivityStopped(p0: Activity) {}
        override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
        override fun onActivityDestroyed(p0: Activity) {}
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(mFTActivityLifecycleCallbacks)
//        val sharedPreferences = getSharedPreferences("Sozo", Context.MODE_PRIVATE)
//        val useMaterialYou = sharedPreferences.getBoolean("use_material_you", true)
//        if (useMaterialYou) {
//            DynamicColors.applyToActivitiesIfAvailable(this)
//            //TODO: HarmonizedColors
//        }
    }

}