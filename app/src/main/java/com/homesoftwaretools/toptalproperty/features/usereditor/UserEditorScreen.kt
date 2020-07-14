package com.homesoftwaretools.toptalproperty.features.usereditor

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.navigator.Navigator
import com.homesoftwaretools.toptalproperty.core.ui.*
import com.homesoftwaretools.toptalproperty.core.utils.ResourceProvider
import com.homesoftwaretools.toptalproperty.core.utils.Toaster
import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.domain.UserRole
import org.koin.core.inject

class UserEditorActivity : BaseActivity() {
    override fun fragmentBuilder() = UserEditorScreen()
}

class UserEditorScreen : BaseFragment() {

    override val layoutId = R.layout.user_editor_screen

    private lateinit var realtorOption: RadioButton
    private lateinit var customerOption: RadioButton
    private lateinit var nameEditor: EditText
    private lateinit var nameLayout: TextInputLayout
    private lateinit var emailEditor: EditText
    private lateinit var emailLayout: TextInputLayout
    private lateinit var deleteButton: View
    private lateinit var saveButton: View
    private lateinit var passwordEditor: EditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var password2Editor: EditText
    private lateinit var password2Layout: TextInputLayout

    private val vm: UserEditorViewModel by scopedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        vm.userData.onChange(this::populate)
        saveButton.onClick { vm.saveUser(collect(), passwordEditor.text.toString(), password2Editor.text.toString()) }
        deleteButton.onClick { authId?.let(vm::deleteUser) }
        vm.nameError.onChange(nameLayout::setError)
        vm.emailError.onChange(emailLayout::setError)
        vm.passwordError.onChange(passwordLayout::setError)
        vm.password2Error.onChange(password2Layout::setError)

        authId
            ?.takeIf { it.isNotBlank() }
            ?.let(vm::loadUser)
            ?: populate(User.EMPTY)
    }

    private val authId: String?
        get() = args("authId")

    private fun initView(v: View) {
        realtorOption = v.findViewById(R.id.realtor_option)
        customerOption = v.findViewById(R.id.customer_option)
        nameEditor = v.findViewById(R.id.name_editor)
        nameLayout = v.findViewById(R.id.name_layout)
        emailEditor = v.findViewById(R.id.email_editor)
        emailLayout = v.findViewById(R.id.email_layout)
        deleteButton = v.findViewById(R.id.delete_button)
        saveButton = v.findViewById(R.id.save_button)
        passwordLayout = v.findViewById(R.id.password_layout)
        passwordEditor = v.findViewById(R.id.password_text)
        password2Layout = v.findViewById(R.id.password2_layout)
        password2Editor = v.findViewById(R.id.password2_text)
    }

    private fun populate(user: User) {
        realtorOption.isChecked = user.role == UserRole.Realtor
        customerOption.isChecked = user.role == UserRole.Client
        realtorOption.isEnabled = user.role != UserRole.Admin
        customerOption.isEnabled = user.role != UserRole.Admin

        nameEditor.setText(user.name)
        emailEditor.setText(user.email)

        passwordLayout.visible = authId.isNullOrBlank()
        password2Layout.visible = authId.isNullOrBlank()
        deleteButton.isEnabled = user.authId.isNotBlank() && user.role != UserRole.Admin
    }

    private fun collect(): User = User(
        authId = authId.orEmpty(),
        name = nameEditor.text.toString(),
        email = emailEditor.text.toString(),
        role = if (realtorOption.isChecked) UserRole.Realtor else UserRole.Client
    )

}

class UserEditorViewModel(scopeId: String) : BaseViewModel(scopeId) {

    private val navigator: Navigator by scope.inject()
    private val toaster: Toaster by scope.inject()
    private val useCase: UserEditorUseCase by inject()
    private val rp: ResourceProvider by inject()

    val userData: LiveData<User> = MutableLiveData()
    val nameError: LiveData<String> = MutableLiveData()
    val emailError: LiveData<String> = MutableLiveData()
    val passwordError: LiveData<String> = MutableLiveData()
    val password2Error: LiveData<String> = MutableLiveData()

    fun loadUser(authId: String) {
        useCase.getUser(authId)
            .doFinally { navigator.hideLoader() }
            .doOnSubscribe { navigator.pushLoader() }
            .bindSubscribe(
                onSuccess = (userData as MutableLiveData)::postValue,
                onError = toaster::showError
            )
    }

    fun saveUser(user: User, password: String, password2: String) {
        if (checkIfEmpty(user.name, nameError, rp) || checkIfEmpty(user.email, emailError, rp))
            return

        if (user.authId.isNotBlank())
            updateUser(user)
        else
            createUser(user, password, password2)
    }

    private fun updateUser(user: User) {
        useCase.getUser(user.authId)
            .flatMap { u -> useCase.saveUser(user.copy(id = u.id)) }
            .doFinally { navigator.hideLoader() }
            .doOnSubscribe { navigator.pushLoader() }
            .bindSubscribe(
                onSuccess = { navigator.popBack() },
                onError = toaster::showError
            )
    }

    private fun createUser(user: User, password: String, password2: String) {
        if (checkIfEmpty(password, passwordError, rp)
            || checkIfNotEquals(password, password2, password2Error))
            return

        useCase.createUser(user, password)
            .doFinally { navigator.hideLoader() }
            .doOnSubscribe { navigator.pushLoader() }
            .bindSubscribe(
                onSuccess = { navigator.popBack() },
                onError = toaster::showError
            )
    }

    private fun checkIfNotEquals(src1: String, src2: String, liveData: LiveData<String>): Boolean {
        return (src1 != src2).apply {
            (liveData as MutableLiveData)
                .postValue(if (this) rp.string(R.string.different_password_error) else "")
        }
    }

    fun deleteUser(authId: String) {
        useCase.deleteUser(authId)
            .doFinally { navigator.hideLoader() }
            .doOnSubscribe { navigator.pushLoader() }
            .bindSubscribe(
                onComplete = { navigator.popBack() },
                onError = toaster::showError
            )

    }

}