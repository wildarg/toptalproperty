package com.homesoftwaretools.toptalproperty.features.editor

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.navigator.Navigator
import com.homesoftwaretools.toptalproperty.core.ui.BaseActivity
import com.homesoftwaretools.toptalproperty.core.ui.BaseFragment
import com.homesoftwaretools.toptalproperty.core.ui.BaseViewModel
import com.homesoftwaretools.toptalproperty.core.ui.onClick
import com.homesoftwaretools.toptalproperty.core.utils.NumberFormatter
import com.homesoftwaretools.toptalproperty.core.utils.ResourceProvider
import com.homesoftwaretools.toptalproperty.core.utils.Toaster
import com.homesoftwaretools.toptalproperty.domain.Apartment
import com.homesoftwaretools.toptalproperty.domain.Location
import com.homesoftwaretools.toptalproperty.logd
import org.koin.android.ext.android.inject
import org.koin.core.inject

class ApartmentEditorActivity : BaseActivity() {
    override fun fragmentBuilder() = ApartmentEditorScreen()
}

class ApartmentEditorScreen : BaseFragment() {
    override val layoutId = R.layout.apart_editor_screen

    private lateinit var nameEditor: TextInputEditText
    private lateinit var nameLayout: TextInputLayout
    private lateinit var descriptionEditor: TextInputEditText
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var areaEditor: TextInputEditText
    private lateinit var areaLayout: TextInputLayout
    private lateinit var roomEditor: TextInputEditText
    private lateinit var roomLayout: TextInputLayout
    private lateinit var priceEditor: TextInputEditText
    private lateinit var priceLayout: TextInputLayout
    private lateinit var addressEditor: TextInputEditText
    private lateinit var addressLayout: TextInputLayout
    private lateinit var latitudeEditor: TextInputEditText
    private lateinit var latitudeLayout: TextInputLayout
    private lateinit var longitudeEditor: TextInputEditText
    private lateinit var longitudeLayout: TextInputLayout
    private lateinit var saveButton: View

    private val fmt: NumberFormatter by inject()
    private val rp: ResourceProvider by inject()
    private val vm: ApartmentEditorViewModel by scopedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)

        addressLayout.setEndIconOnClickListener { logd { "Click" } }
        saveButton.setOnClickListener(this::onSaveClick)
        vm.apartment.onChange(this::populate)

        vm.loadApartment("qG8dFiJ6BzIvgLHItyYu")
    }

    private fun onSaveClick(v: View) {
        if (checkForm())
            vm.saveApartment(collectData())
    }

    private fun initView(v: View) {
        nameEditor = v.findViewById(R.id.name_editor)
        nameLayout = v.findViewById(R.id.name_layout)
        descriptionEditor = v.findViewById(R.id.description_editor)
        descriptionLayout = v.findViewById(R.id.description_layout)
        areaEditor = v.findViewById(R.id.area_editor)
        areaLayout = v.findViewById(R.id.area_layout)
        roomEditor = v.findViewById(R.id.rooms_editor)
        roomLayout = v.findViewById(R.id.rooms_layout)
        priceEditor = v.findViewById(R.id.price_editor)
        priceLayout = v.findViewById(R.id.price_layout)
        addressEditor = v.findViewById(R.id.address_editor)
        addressLayout = v.findViewById(R.id.address_layout)
        latitudeEditor = v.findViewById(R.id.latitude_editor)
        latitudeLayout = v.findViewById(R.id.latitude_layout)
        longitudeEditor = v.findViewById(R.id.longitude_editor)
        longitudeLayout = v.findViewById(R.id.longitude_layout)
        saveButton = v.findViewById(R.id.save_button)
    }

    private fun populate(apartment: Apartment) {
        nameEditor.setText(apartment.name)
        descriptionEditor.setText(apartment.description)
        areaEditor.setText(fmt.formatNum(apartment.area))
        roomEditor.setText(fmt.formatInt(apartment.rooms))
        priceEditor.setText(fmt.formatCurrency(apartment.price))
        latitudeEditor.setText(fmt.formatNum(apartment.location.latitude))
        longitudeEditor.setText(fmt.formatNum(apartment.location.longitude))
    }

    private fun checkForm(): Boolean {
        val oneEmpty = checkIfEmpty(nameEditor, nameLayout)
                || checkIfEmpty(areaEditor, areaLayout)
                || checkIfEmpty(roomEditor, roomLayout)
                || checkIfEmpty(priceEditor, priceLayout)
                || checkIfEmpty(latitudeEditor, latitudeLayout)
                || checkIfEmpty(longitudeEditor, longitudeLayout)
        return !oneEmpty
    }

    private fun checkIfEmpty(editor: TextInputEditText, layout: TextInputLayout): Boolean =
        editor.text.isNullOrBlank().apply {
            layout.error = if (this) rp.string(R.string.required_field_error) else ""
        }

    private fun collectData(): Apartment {
        return Apartment(
            name = nameEditor.text.toString(),
            description = descriptionEditor.text.toString(),
            area = fmt.parseNum(areaEditor.text.toString()),
            rooms = fmt.parseInt(roomEditor.text.toString()),
            price = fmt.parseNum(priceEditor.text.toString()),
            location = Location(
                latitude = fmt.parseNum(latitudeEditor.text.toString()),
                longitude = fmt.parseNum(longitudeEditor.text.toString())
            )
        )
    }
}

class ApartmentEditorViewModel(scopeId: String) : BaseViewModel(scopeId) {

    private val toaster: Toaster by scope.inject()
    private val navigator: Navigator by scope.inject()
    private val useCase: ApartmentEditorUseCase by inject()

    val apartment: LiveData<Apartment> = MutableLiveData()

    fun loadApartment(id: String) {
        useCase.load(id)
            .doOnSubscribe { navigator.pushLoader() }
            .doFinally { navigator.hideLoader() }
            .bindSubscribe(
                onSuccess = (apartment as MutableLiveData)::postValue,
                onError = toaster::showError
            )
    }

    fun saveApartment(data: Apartment) {
        useCase.save(data)
            .doOnSubscribe { navigator.pushLoader() }
            .doFinally { navigator.hideLoader() }
            .bindSubscribe(
                onSuccess = { logd { "OK" } },
                onError = toaster::showError
            )
    }

}