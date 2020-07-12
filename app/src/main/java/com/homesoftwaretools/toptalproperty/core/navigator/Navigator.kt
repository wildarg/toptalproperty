package com.homesoftwaretools.toptalproperty.core.navigator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.ui.BaseActivity
import com.homesoftwaretools.toptalproperty.core.ui.LoaderScreen
import com.homesoftwaretools.toptalproperty.features.dashboard.DashboardActivity
import com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist.ApartmentListScreen
import com.homesoftwaretools.toptalproperty.features.dashboard.map.ApartmentMapScreen
import com.homesoftwaretools.toptalproperty.features.editor.ApartmentEditorActivity
import com.homesoftwaretools.toptalproperty.features.welcome.WelcomeActivity
import com.homesoftwaretools.toptalproperty.features.welcome.login.LoginFragment
import com.homesoftwaretools.toptalproperty.logd
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass

interface Navigator {
    fun push(target: Fragment, replace: Boolean = true, stack: String = "", args: Map<String, String> = emptyMap())
    fun push(name: String, args: Map<String, String> = emptyMap(), cached: Boolean = false)
    fun popBack()
    fun pushLoader()
    fun hideLoader()
}

class AppNavigator(private val context: Context) : Navigator {

    private val fragmentCache = HashMap<String, Fragment>()

    private val routes by lazy {
        mapOf(
            Routes.WELCOME to ActivityRoute(WelcomeActivity::class, clearStack = true),
            Routes.LOG_IN to FragmentRoute { LoginFragment() },
            Routes.DASHBOARD to ActivityRoute(DashboardActivity::class, clearStack = true),
            Routes.APARTMENT_EDITOR to ActivityRoute(ApartmentEditorActivity::class),
            Routes.APARTMENT_LIST to FragmentRoute { ApartmentListScreen() },
            Routes.APARTMENT_MAP to FragmentRoute { ApartmentMapScreen() }
        )
    }

    fun toBundle(args: Map<String, String>): Bundle {
        return Bundle()
            .apply { args.entries.forEach { (key, vaue) -> putString(key, vaue) } }
    }

    override fun push(target: Fragment, replace: Boolean, stack: String, args: Map<String, String>) {
        val mgr = (context as? BaseActivity)?.supportFragmentManager ?: return
        target.arguments = toBundle(args)
        mgr.beginTransaction()
            .apply { if (replace) this.replace(R.id.container, target) else this.add(R.id.container, target) }
            .addToBackStack(stack)
            .commit()
    }

    private fun startActivity(
        route: ActivityRoute,
        args: Map<String, String>
    ) {
        val intent = Intent(context, route.activityClass.java)
        args.entries.forEach { (key, value) -> intent.putExtra(key, value) }
        if (route.clearStack)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP + Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (route.clearStack)
            (context as? BaseActivity)?.finish()
    }

    private fun getFragment(name: String, route: FragmentRoute, cached: Boolean): Fragment {
        return if (!cached) route.builder()
        else
            name.toLowerCase(Locale.getDefault()).let { n ->
                fragmentCache[n]
                    ?: route.builder().apply { fragmentCache[n] = this }
            }
    }

    override fun push(name: String, args: Map<String, String>, cached: Boolean) {
        when (val route = routes[name.toLowerCase(Locale.getDefault())]) {
            is FragmentRoute -> push(target = getFragment(name, route, cached), args = args)
            is ActivityRoute -> startActivity(route, args)
            else -> logd { "Unknown route $name" }
        }
    }

    override fun popBack() {
        (context as? BaseActivity)?.finish()
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