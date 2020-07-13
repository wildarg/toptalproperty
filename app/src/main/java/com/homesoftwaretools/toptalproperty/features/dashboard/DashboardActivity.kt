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
import com.homesoftwaretools.toptalproperty.logd

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

    override fun onBottomItemSelect(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.list_page  -> vm.openList().also { showFab() }
            R.id.map_page   -> vm.openMap().also { hideFab() }
        }
        return true
    }
}

class DashboardViewModel(scopeId: String) : BaseViewModel(scopeId) {

    private val navigator: Navigator by scope.inject()

    fun createApartment() {
        navigator.push(Routes.APARTMENT_EDITOR)
    }

    fun openMap() {
        navigator.push(Routes.APARTMENT_MAP)
    }

    fun openList() {
        navigator.push(Routes.APARTMENT_LIST)
    }

}