package com.homesoftwaretools.toptalproperty.core.navigator

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.ui.BaseActivity
import com.homesoftwaretools.toptalproperty.core.ui.LoaderScreen
import com.homesoftwaretools.toptalproperty.features.dashboard.DashboardActivity
import com.homesoftwaretools.toptalproperty.features.welcome.WelcomeActivity
import com.homesoftwaretools.toptalproperty.features.welcome.login.LoginFragment
import com.homesoftwaretools.toptalproperty.logd
import java.util.*
import kotlin.reflect.KClass

interface Navigator {
    fun push(target: Fragment, replace: Boolean = true, stack: String = "")
    fun push(name: String)
    fun pushLoader()
    fun hideLoader()
}

class AppNavigator(private val context: Context) : Navigator {

    init {
        logd { "AppNavigator:: Created with $context" }
    }

    private val routes by lazy {
        mapOf(
            Routes.WELCOME to ActivityRoute(WelcomeActivity::class, clearStack = true),
            Routes.LOG_IN to FragmentRoute { LoginFragment() },
            Routes.DASHBOARD to ActivityRoute(DashboardActivity::class, clearStack = true)
        )
    }

    override fun push(target: Fragment, replace: Boolean, stack: String) {
        val mgr = (context as? BaseActivity)?.supportFragmentManager ?: return
        mgr.beginTransaction()
            .apply { if (replace) this.replace(R.id.container, target) else this.add(R.id.container, target) }
            .addToBackStack(stack)
            .commit()
    }

    private fun startActivity(route: ActivityRoute) {
        val intent = Intent(context, route.activityClass.java)
        if (route.clearStack)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP + Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (route.clearStack)
            (context as? BaseActivity)?.finish()
    }

    override fun push(name: String) {
        when (val route = routes[name.toLowerCase(Locale.getDefault())]) {
            is FragmentRoute -> push(target = route.builder())
            is ActivityRoute -> startActivity(route)
            else -> logd { "Unknown route $name" }
        }
    }

    override fun pushLoader() {
        push(LoaderScreen(), replace = false, stack = "loader")
    }

    override fun hideLoader() {
        val mgr = (context as? BaseActivity)?.supportFragmentManager ?: return
        mgr.popBackStack("loader", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}

sealed class Route

data class FragmentRoute(val builder: () -> Fragment) : Route()
data class ActivityRoute(
    val activityClass: KClass<*>,
    val clearStack: Boolean = false
) : Route()