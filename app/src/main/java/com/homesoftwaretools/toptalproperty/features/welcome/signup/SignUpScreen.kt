package com.homesoftwaretools.toptalproperty.features.welcome.signup

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.navigator.Navigator
import com.homesoftwaretools.toptalproperty.core.navigator.Routes
import com.homesoftwaretools.toptalproperty.core.ui.BaseFragment
import com.homesoftwaretools.toptalproperty.core.ui.BaseViewModel
import com.homesoftwaretools.toptalproperty.core.ui.onClick
import com.homesoftwaretools.toptalproperty.core.utils.ResourceProvider
import com.homesoftwaretools.toptalproperty.core.utils.Toaster
import com.homesoftwaretools.toptalproperty.logd
import org.koin.core.inject

class SignUpScreen : BaseFragment() {

    private lateinit var email: EditText
    private lateinit var emailLayout: TextInputLayout
    private lateinit var password: EditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var password2: EditText
    private lateinit var password2Layout: TextInputLayout
    private lateinit var signUpButton: View

    override val layoutId = R.layout.signup_screen
    private val vm: SignUpViewModel by scopedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        vm.nameError.onChange(emailLayout::setError)
        vm.passwordError.onChange(passwordLayout::setError)
        vm.password2Error.onChange(password2Layout::setError)
        signUpButton.onClick {
            vm.signUp(email.text.toString(), password.text.toString(), password2.text.toString())
        }
    }

    private fun initView(v: View) {
        email = v.findViewById(R.id.email_text)
        emailLayout = v.findViewById(R.id.email_layout)
        password = v.findViewById(R.id.password_text)
        passwordLayout = v.findViewById(R.id.password_layout)
        password2 = v.findViewById(R.id.password2_text)
        password2Layout = v.findViewById(R.id.password2_layout)
        signUpButton = v.findViewById(R.id.sign_up_button)
    }
}

class SignUpViewModel(scopeId: String) : BaseViewModel(scopeId) {
    private val navigator: Navigator by scope.inject()
    private val toaster: Toaster by scope.inject()
    private val rp: ResourceProvider by inject()
    private val useCase: SignUpUseCase by inject()

    val nameError: LiveData<String> = MutableLiveData()
    val passwordError: LiveData<String> = MutableLiveData()
    val password2Error: LiveData<String> = MutableLiveData()

    fun signUp(email: String, password: String, password2: String) {
        if (checkIfEmpty(email, nameError, rp)
            || checkIfEmpty(password, passwordError, rp)
            || checkIfNotEquals(password, password2, password2Error))
            return

        useCase.signUp(email, password)
            .doFinally { navigator.hideLoader() }
            .doOnSubscribe { navigator.pushLoader() }
            .bindSubscribe(
                onComplete = { navigator.push(Routes.REGISTER) },
                onError = toaster::showError
            )
    }

    private fun checkIfNotEquals(src1: String, src2: String, liveData: LiveData<String>): Boolean {
        return (src1 != src2).apply {
            (liveData as MutableLiveData)
                .postValue(if (this) rp.string(R.string.different_password_error) else "")
        }
    }


}