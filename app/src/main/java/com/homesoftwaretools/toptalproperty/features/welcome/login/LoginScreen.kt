package com.homesoftwaretools.toptalproperty.features.welcome.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.navigator.Navigator
import com.homesoftwaretools.toptalproperty.core.navigator.Routes
import com.homesoftwaretools.toptalproperty.core.ui.BaseFragment
import com.homesoftwaretools.toptalproperty.core.ui.BaseViewModel
import com.homesoftwaretools.toptalproperty.core.ui.onClick
import com.homesoftwaretools.toptalproperty.core.utils.ResourceProvider
import com.homesoftwaretools.toptalproperty.core.utils.Toaster
import com.homesoftwaretools.toptalproperty.repo.dao.UserNotFoundException
import org.koin.core.inject

class LoginScreen : BaseFragment() {

    override val layoutId: Int = R.layout.login_screen
    private val vm: LoginViewModel by scopedViewModel()
    lateinit var email: TextInputEditText
    lateinit var emailLayout: TextInputLayout
    lateinit var password: TextInputEditText
    lateinit var passwordLayout: TextInputLayout
    lateinit var loginButton: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        checkArgs()

        loginButton.onClick {
            vm.logIn(email.text.toString(), password.text.toString())
        }
        vm.nameError.onChange { emailLayout.error = it }
        vm.passwordError.onChange { passwordLayout.error = it }
    }

    private fun checkArgs() {
        args("email")?.takeIf { it.isNotBlank() }
            ?.let {
                email.setText(it)
                password.requestFocus()
            }
    }

    private fun initView(view: View) {
        email = view.findViewById(R.id.email_text)
        emailLayout = view.findViewById(R.id.email_layout)
        password = view.findViewById(R.id.password_text)
        passwordLayout = view.findViewById(R.id.password_layout)
        loginButton = view.findViewById(R.id.login_button)
    }
}

class LoginViewModel(scopeID: String) : BaseViewModel(scopeID) {

    private val navigator: Navigator by scope.inject()
    private val toaster: Toaster by scope.inject()
    private val useCase: LoginUseCase by inject()
    private val rp: ResourceProvider by inject()

    val nameError: LiveData<String> = MutableLiveData()
    val passwordError: LiveData<String> = MutableLiveData()

    fun logIn(name: String, password: String) {
        if (checkIfEmpty(name, nameError, rp) || checkIfEmpty(password, passwordError, rp))
            return

        useCase.login(name, password)
            .doFinally { navigator.hideLoader() }
            .doOnSubscribe { navigator.pushLoader() }
            .bindSubscribe(
                onSuccess = { navigator.push(Routes.DASHBOARD) },
                onError = {
                    if (it is UserNotFoundException)
                        navigator.push(Routes.REGISTER)
                    else
                        toaster.toast(it.message)
                }
            )
    }


}