package com.homesoftwaretools.toptalproperty.features.usercard

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.navigator.Navigator
import com.homesoftwaretools.toptalproperty.core.navigator.Routes
import com.homesoftwaretools.toptalproperty.core.ui.BaseFragment
import com.homesoftwaretools.toptalproperty.core.ui.BaseViewModel
import com.homesoftwaretools.toptalproperty.core.ui.onClick
import com.homesoftwaretools.toptalproperty.core.utils.ResourceProvider
import com.homesoftwaretools.toptalproperty.core.utils.Toaster
import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.domain.UserRole
import com.homesoftwaretools.toptalproperty.repo.googlemap.GoogleGeocodeRepo
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.inject
import org.koin.core.qualifier.named
import org.koin.core.scope.ScopeID

class UserCard : BaseFragment() {
    override val layoutId = R.layout.user_card

    private val vm: UserCardViewModel by scopedViewModel()

    lateinit var signOutButton: View
    lateinit var userName: TextView
    lateinit var userRole: TextView
    lateinit var email: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)

        signOutButton.onClick { vm.signOut() }
        vm.user.onChange { user ->
            userName.text = user.name
            userRole.text = vm.userRoleName(user.role)
            email.text = user.email
        }
    }

    private fun initView(v: View) {
        signOutButton = v.findViewById(R.id.sign_out_button)
        userName = v.findViewById(R.id.user_name)
        userRole = v.findViewById(R.id.user_role)
        email = v.findViewById(R.id.user_email)
    }

}

class UserCardViewModel(scopeID: String) : BaseViewModel(scopeID) {

    private val toaster: Toaster by scope.inject()
    private val navigator: Navigator by scope.inject()
    private val useCase: UserCardUseCase by inject()
    private val rp: ResourceProvider by inject()

    val user: LiveData<User>
        get() = userData

    private val userData = MutableLiveData<User>()

    init {
        useCase.getCurrentUser()
            .bindSubscribe(
                onSuccess = userData::postValue,
                onError = toaster::showError
            )
    }

    fun signOut() {
        useCase.signOff()
            .bindSubscribe(
                onComplete = { navigator.push(Routes.WELCOME) },
                onError = toaster::showError
            )
    }

    fun userRoleName(role: UserRole): String = when (role) {
        UserRole.Admin -> rp.string(R.string.admin_role_name)
        UserRole.Client -> rp.string(R.string.customer_role_name)
        UserRole.Realtor -> rp.string(R.string.realtor_role_name)
    }

}