package com.homesoftwaretools.toptalproperty.features.dashboard

import android.view.MenuItem
import android.view.View
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.navigator.Navigator
import com.homesoftwaretools.toptalproperty.core.navigator.Routes
import com.homesoftwaretools.toptalproperty.core.ui.BaseScaffoldActivity
import com.homesoftwaretools.toptalproperty.core.ui.BaseViewModel
import com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist.ApartmentListScreen
import com.homesoftwaretools.toptalproperty.features.drawer.NavigationDrawer

class DashboardActivity : BaseScaffoldActivity() {

    override val titleResId = R.string.property_list_title
    override val menuResId = R.menu.drawer_menu
    override val bottomMenuResId = R.menu.dashboard_nav_menu

    override fun fragmentBuilder() = ApartmentListScreen()
    override fun drawerBuilder() = NavigationDrawer()

    private val vm: DashboardViewModel by scopedViewModel()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        openDrawer()
        return true
    }

    override fun onFabClick(view: View) {
        vm.createApartment()
    }
}

class DashboardViewModel(scopeId: String) : BaseViewModel(scopeId) {

    private val navigator: Navigator by scope.inject()

    fun createApartment() {
        navigator.push(Routes.APARTMENT_EDITOR)
    }

}