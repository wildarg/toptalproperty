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
import org.koin.core.inject

class LoginFragment : BaseFragment() {

    override val layoutId: Int = R.layout.login_screen
    private val vm: LoginViewModel by scopedViewModel()
    lateinit var email: TextInputEditText
    lateinit var emailLayout: TextInputLayout
    lateinit var password: TextInputEditText
    lateinit var passwordLayout: TextInputLayout
    lateinit var loginButton: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = view.findViewById(R.id.email_text)
        emailLayout = view.findViewById(R.id.email_layout)
        password = view.findViewById(R.id.password_text)
        passwordLayout = view.findViewById(R.id.password_layout)
        loginButton = view.findViewById(R.id.login_button)

        loginButton.onClick {
            vm.logIn(email.text.toString(), password.text.toString())
        }
        vm.nameError.onChange { emailLayout.error = it }
        vm.passwordError.onChange { passwordLayout.error = it }
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
        if (checkIfEmpty(name, nameError) || checkIfEmpty(password, passwordError))
            return

        useCase.login(name, password)
            .doFinally { navigator.hideLoader() }
            .doOnSubscribe { navigator.pushLoader() }
            .bindSubscribe(
                onSuccess = { navigator.push(Routes.DASHBOARD) },
                onError = { toaster.toast(it.message) }
            )
    }

    private fun checkIfEmpty(src: String, liveData: LiveData<String>): Boolean {
        return src.isBlank().apply {
            (liveData as MutableLiveData)
                .postValue(if (this) rp.string(R.string.required_field_error) else "")
        }
    }

}