package com.homesoftwaretools.toptalproperty.features.welcome

import android.os.Bundle
import android.view.View
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.navigator.Navigator
import com.homesoftwaretools.toptalproperty.core.navigator.Routes
import com.homesoftwaretools.toptalproperty.core.ui.BaseFragment
import com.homesoftwaretools.toptalproperty.core.ui.BaseViewModel
import com.homesoftwaretools.toptalproperty.core.ui.onClick

class WelcomeFragment : BaseFragment() {

    override val layoutId: Int = R.layout.welcome_screen

    private val vm: WelcomeViewModel by scopedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.login_button).onClick {
            vm.logIn()
        }
        view.findViewById<View>(R.id.sign_up_button).onClick {
            vm.signUp()
        }
    }

}

class WelcomeViewModel(scopeID: String) : BaseViewModel(scopeID) {

    private val navigator: Navigator by scope.inject()

    fun logIn() {
        navigator.push(Routes.LOG_IN)
    }

    fun signUp() {
        navigator.push(Routes.SIGN_UP)
    }



}

