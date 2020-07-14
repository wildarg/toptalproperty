package com.homesoftwaretools.toptalproperty.features.dashboard

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.navigator.Navigator
import com.homesoftwaretools.toptalproperty.core.navigator.Routes
import com.homesoftwaretools.toptalproperty.core.ui.BaseScaffoldActivity
import com.homesoftwaretools.toptalproperty.core.ui.BaseViewModel
import com.homesoftwaretools.toptalproperty.core.utils.Toaster
import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.domain.UserRole
import com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist.ApartmentListScreen
import com.homesoftwaretools.toptalproperty.features.drawer.NavigationDrawer
import com.homesoftwaretools.toptalproperty.logd
import com.homesoftwaretools.toptalproperty.repo.UserRepo
import org.koin.core.inject

class DashboardActivity : BaseScaffoldActivity() {

    override val titleResId = R.string.property_list_title
    override val menuResId = R.menu.drawer_menu
    override val bottomMenuResId = R.menu.dashboard_nav_menu

    override fun fragmentBuilder() = ApartmentListScreen()
    override fun drawerBuilder() = NavigationDrawer()

    private val vm: DashboardViewModel by scopedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.currentUser.observe(this, Observer(this::onUserUpdate))
    }

    private fun onUserUpdate(user: User) {
        if (user.role == UserRole.Admin)
            inflateBottomMenu(R.menu.dashboard_nav_admin_menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        openDrawer()
        return true
    }

    override fun onBottomItemSelect(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.list_page  -> vm.openList()
            R.id.map_page   -> vm.openMap()
            R.id.users_page -> vm.openUsers()
        }
        return true
    }
}

class DashboardViewModel(scopeId: String) : BaseViewModel(scopeId) {

    private val navigator: Navigator by scope.inject()
    private val userRepo: UserRepo by inject()
    private val toaster: Toaster by scope.inject()

    val currentUser: LiveData<User> = MutableLiveData()

    init {
        userRepo.getCurrentUser()
            .bindSubscribe(
                onSuccess = (currentUser as MutableLiveData)::postValue,
                onError = toaster::showError
            )
    }

    fun openMap() {
        navigator.push(Routes.APARTMENT_MAP)
    }

    fun openUsers() {
        navigator.push(Routes.USER_LIST)
    }

    fun openList() {
        navigator.push(Routes.APARTMENT_LIST)
    }

}