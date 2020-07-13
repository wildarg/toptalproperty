package com.homesoftwaretools.toptalproperty.features.welcome.register

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
import com.homesoftwaretools.toptalproperty.core.ui.BaseActivity
import com.homesoftwaretools.toptalproperty.core.ui.BaseFragment
import com.homesoftwaretools.toptalproperty.core.ui.BaseViewModel
import com.homesoftwaretools.toptalproperty.core.ui.onClick
import com.homesoftwaretools.toptalproperty.core.utils.ResourceProvider
import com.homesoftwaretools.toptalproperty.core.utils.Toaster
import com.homesoftwaretools.toptalproperty.domain.UserRole
import org.koin.core.inject

class RegisterActivity : BaseActivity() {
    override fun fragmentBuilder() = RegisterScreen()
}

class RegisterScreen : BaseFragment() {

    private lateinit var realtorRole: RadioButton
    private lateinit var customerRole: RadioButton
    private lateinit var userName: EditText
    private lateinit var nameLayout: TextInputLayout
    private lateinit var registerButton: View
    private lateinit var signOutButton: View

    override val layoutId = R.layout.registration_screen
    private val vm: RegisterViewModel by scopedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)

        signOutButton.onClick { vm.signOut() }
        registerButton.onClick { vm.register(getRole(), userName.text.toString()) }
        vm.nameError.onChange(nameLayout::setError)
    }

    private fun getRole(): UserRole {
        return if (realtorRole.isChecked) UserRole.Realtor else UserRole.Client
    }

    private fun initView(v: View) {
        realtorRole = v.findViewById(R.id.realtor_option)
        customerRole = v.findViewById(R.id.customer_option)
        userName = v.findViewById(R.id.name_editor)
        nameLayout = v.findViewById(R.id.name_layout)
        registerButton = v.findViewById(R.id.register_button)
        signOutButton = v.findViewById(R.id.sign_out_button)
    }
}

class RegisterViewModel(scopeId: String) : BaseViewModel(scopeId) {

    private val navigator: Navigator by scope.inject()
    private val toaster: Toaster by scope.inject()
    private val useCase: RegisterUseCase by inject()
    private val rp: ResourceProvider by inject()

    val nameError: LiveData<String> = MutableLiveData()

    fun signOut() {
        useCase.signOut()
            .bindSubscribe(
                onComplete = { navigator.push(Routes.WELCOME) },
                onError = toaster::showError
            )
    }

    fun register(role: UserRole, name: String) {
        if (checkIfEmpty(name, nameError, rp))
            return

        useCase.registerNewUser(role, name)
            .doFinally { navigator.hideLoader() }
            .doOnSubscribe { navigator.pushLoader() }
            .bindSubscribe(
                onSuccess = { navigator.push(Routes.DASHBOARD) },
                onError = toaster::showError
            )
    }

}