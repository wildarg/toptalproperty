package com.homesoftwaretools.toptalproperty.core.navigator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.ui.BaseActivity
import com.homesoftwaretools.toptalproperty.core.ui.BaseScaffoldActivity
import com.homesoftwaretools.toptalproperty.core.ui.LoaderScreen
import com.homesoftwaretools.toptalproperty.features.dashboard.DashboardActivity
import com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist.ApartmentListScreen
import com.homesoftwaretools.toptalproperty.features.dashboard.map.ApartmentMapScreen
import com.homesoftwaretools.toptalproperty.features.editor.ApartmentEditorActivity
import com.homesoftwaretools.toptalproperty.features.userlist.UserListScreen
import com.homesoftwaretools.toptalproperty.features.welcome.WelcomeActivity
import com.homesoftwaretools.toptalproperty.features.welcome.login.LoginScreen
import com.homesoftwaretools.toptalproperty.features.welcome.register.RegisterActivity
import com.homesoftwaretools.toptalproperty.features.welcome.signup.SignUpScreen
import com.homesoftwaretools.toptalproperty.logd
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass


interface Navigator {
    fun push(target: Fragment, replace: Boolean = true, stack: String? = "", args: Map<String, String> = emptyMap())
    fun push(name: String, args: Map<String, String> = emptyMap(), cached: Boolean = false)
    fun popBack()
    fun pushLoader()
    fun hideLoader()
    fun openDrawer()
    fun hideDrawer()
    fun hideSoftKeyboard(view: View)
}

class AppNavigator(private val context: Context) : Navigator {

    private val fragmentCache = HashMap<String, Fragment>()

    private val routes by lazy {
        mapOf(
            Routes.WELCOME to ActivityRoute(WelcomeActivity::class, clearStack = true),
            Routes.LOG_IN to FragmentRoute { LoginScreen() },
            Routes.SIGN_UP to FragmentRoute { SignUpScreen() },
            Routes.REGISTER to ActivityRoute(RegisterActivity::class, clearStack = true),
            Routes.DASHBOARD to ActivityRoute(DashboardActivity::class, clearStack = true),
            Routes.APARTMENT_EDITOR to ActivityRoute(ApartmentEditorActivity::class),
            Routes.APARTMENT_LIST to FragmentRoute(null) { ApartmentListScreen() },
            Routes.APARTMENT_MAP to FragmentRoute(null) { ApartmentMapScreen() },
            Routes.USER_LIST to FragmentRoute(null) { UserListScreen() }
        )
    }

    fun toBundle(args: Map<String, String>): Bundle {
        return Bundle()
            .apply { args.entries.forEach { (key, vaue) -> putString(key, vaue) } }
    }

    override fun push(target: Fragment, replace: Boolean, stack: String?, args: Map<String, String>) {
        val mgr = (context as? BaseActivity)?.supportFragmentManager ?: return
        target.arguments = toBundle(args)
        mgr.beginTransaction()
            .apply { if (replace) this.replace(R.id.container, target) else this.add(R.id.container, target) }
            .apply { if (stack != null) addToBackStack(stack) }
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
        logd { "NAV:: Navigate to route $name with args: $args" }
        when (val route = routes[name.toLowerCase(Locale.getDefault())]) {
            is FragmentRoute -> push(target = getFragment(name, route, cached), args = args, stack = route.stack)
            is ActivityRoute -> startActivity(route, args)
            else -> logd { "Unknown route $name" }
        }
    }

    override fun popBack() {
        val activity = context as? BaseActivity
        activity ?: return
        if (!activity.isFinishing)
            activity.finish()
    }

    override fun pushLoader() {
        push(LoaderScreen(), replace = false, stack = "loader")
    }

    override fun hideLoader() {
        val mgr = (context as? BaseActivity)?.supportFragmentManager ?: return
        mgr.popBackStack("loader", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun openDrawer() {
        (context as? BaseScaffoldActivity)?.openDrawer()
    }

    override fun hideDrawer() {
        (context as? BaseScaffoldActivity)?.hideDrawer()
    }

    override fun hideSoftKeyboard(view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

sealed class Route

data class FragmentRoute(val stack: String? = "", val builder: () -> Fragment) : Route()
data class ActivityRoute(
    val activityClass: KClass<*>,
    val clearStack: Boolean = false
) : Route()