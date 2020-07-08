package com.homesoftwaretools.toptalproperty

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.homesoftwaretools.toptalproperty.di.appModule
import com.homesoftwaretools.toptalproperty.di.repoModule
import com.homesoftwaretools.toptalproperty.di.useCases
import com.homesoftwaretools.toptalproperty.di.utils
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TopTalPropertyApp : Application() {

    private val activityCallback by lazy { ActivityLifecycleCallback() }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            logger(AndroidLogger())
            androidLogger(level = Level.DEBUG)
            androidContext(this@TopTalPropertyApp)
            modules(listOf(appModule, useCases, repoModule, utils))
        }

        registerActivityLifecycleCallbacks(activityCallback)
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterActivityLifecycleCallbacks(activityCallback)
    }

}

fun logd(block: () -> String) {
    Log.d("[logd]", block())
}

class ActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(p0: Activity) {
        logd { "onActivityPaused - $p0" }
    }

    override fun onActivityStarted(p0: Activity) {
        logd { "onActivityStarted - $p0" }
    }

    override fun onActivityDestroyed(p0: Activity) {
        logd { "onActivityDestroyed - $p0" }
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        logd { "onActivitySaveInstanceState - $p0" }
    }

    override fun onActivityStopped(p0: Activity) {
        logd { "onActivityStopped - $p0" }
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        logd { "onActivityCreated - $p0" }
    }

    override fun onActivityResumed(p0: Activity) {
        logd { "onActivityResumed - $p0" }
    }

}

class FragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
        logd { "onFragmentViewCreated - $f" }
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        super.onFragmentStopped(fm, f)
        logd { "onFragmentStopped - $f" }
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, f, savedInstanceState)
        logd { "onFragmentCreated - $f" }
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        super.onFragmentResumed(fm, f)
        logd { "onFragmentResumed - $f" }
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        super.onFragmentAttached(fm, f, context)
        logd { "onFragmentAttached - $f" }
    }

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        super.onFragmentPreAttached(fm, f, context)
        logd { "onFragmentPreAttached - $f" }
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentDestroyed(fm, f)
        logd { "onFragmentDestroyed - $f" }
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        super.onFragmentSaveInstanceState(fm, f, outState)
        logd { "onFragmentSaveInstanceState - $f" }
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        super.onFragmentStarted(fm, f)
        logd { "onFragmentStarted - $f" }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentViewDestroyed(fm, f)
        logd { "onFragmentViewDestroyed - $f" }
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentPreCreated(fm, f, savedInstanceState)
        logd { "onFragmentPreCreated - $f" }
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentActivityCreated(fm, f, savedInstanceState)
        logd { "onFragmentActivityCreated - $f" }
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        super.onFragmentPaused(fm, f)
        logd { "onFragmentPaused - $f" }
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        super.onFragmentDetached(fm, f)
        logd { "onFragmentDetached - $f" }
    }
}