package com.homesoftwaretools.toptalproperty.features.dashboard

import android.view.MenuItem
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.ui.BaseScaffoldActivity
import com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist.ApartmentListScreen
import com.homesoftwaretools.toptalproperty.features.drawer.NavigationDrawer

class DashboardActivity : BaseScaffoldActivity() {

    override val titleResId = R.string.property_list_title
    override val menuResId = R.menu.drawer_menu
    override val bottomMenuResId = R.menu.dashboard_nav_menu

    override fun fragmentBuilder() = ApartmentListScreen()
    override fun drawerBuilder() = NavigationDrawer()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        openDrawer()
        return true
    }
}