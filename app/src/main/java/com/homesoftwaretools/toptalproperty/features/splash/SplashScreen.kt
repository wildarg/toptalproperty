package com.homesoftwaretools.toptalproperty.features.splash

import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.navigator.Navigator
import com.homesoftwaretools.toptalproperty.core.navigator.Routes
import com.homesoftwaretools.toptalproperty.core.ui.BaseActivity
import com.homesoftwaretools.toptalproperty.core.ui.BaseFragment
import com.homesoftwaretools.toptalproperty.core.ui.BaseViewModel
import com.homesoftwaretools.toptalproperty.logd
import com.homesoftwaretools.toptalproperty.repo.dao.UserNotFoundException
import org.koin.core.inject
import org.koin.core.scope.ScopeID

class SplashScreen : BaseActivity() {
    override fun fragmentBuilder() = SplashFragment()
}

class SplashFragment : BaseFragment() {
    override val layoutId = R.layout.splash_screen
    private val vm: SplashViewModel by scopedViewModel()

    override fun onResume() {
        super.onResume()
        vm.checkIfAlreadyLogin()
    }
}

class SplashViewModel(scopeID: ScopeID) : BaseViewModel(scopeID) {

    private val navigator: Navigator by scope.inject()
    private val useCase: SplashUseCase by inject()

    fun checkIfAlreadyLogin() {
        useCase.isLoggedIn()
            .bindSubscribe(
                onSuccess = {
                    if (it) navigator.push(Routes.DASHBOARD)
                    else navigator.push(Routes.WELCOME)
                },
                onError = { cause ->
                    if (cause is UserNotFoundException)
                        navigator.push(Routes.REGISTER)
                    else
                        navigator.push(Routes.WELCOME)
                }
            )
    }

}