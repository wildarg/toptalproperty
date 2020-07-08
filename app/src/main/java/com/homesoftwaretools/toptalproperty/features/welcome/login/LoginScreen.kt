package com.homesoftwaretools.toptalproperty.features.welcome.login

import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.navigator.Navigator
import com.homesoftwaretools.toptalproperty.core.navigator.Routes
import com.homesoftwaretools.toptalproperty.core.ui.BaseFragment
import com.homesoftwaretools.toptalproperty.core.ui.BaseViewModel
import com.homesoftwaretools.toptalproperty.core.ui.onClick
import com.homesoftwaretools.toptalproperty.core.utils.Toaster
import com.homesoftwaretools.toptalproperty.logd
import org.koin.core.inject

class LoginFragment : BaseFragment() {

    override val layoutId: Int = R.layout.login_screen
    private val vm: LoginViewModel by scopedViewModel()
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var loginButton: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = view.findViewById(R.id.email_text)
        password = view.findViewById(R.id.password_text)
        loginButton = view.findViewById(R.id.login_button)

        loginButton.onClick {
            vm.logIn(email.text.toString(), password.text.toString())
        }
    }
}

class LoginViewModel(scopeID: String) : BaseViewModel(scopeID) {

    private val navigator: Navigator by scope.inject()
    private val toaster: Toaster by scope.inject()
    private val useCase: LoginUseCase by inject()

    fun logIn(name: String, password: String) {
        useCase.login(name, password)
            .bindSubscribe(
                onSuccess = { navigator.push(Routes.DASHBOARD) },
                onError = { toaster.toast(it.message) }
            )
    }

}