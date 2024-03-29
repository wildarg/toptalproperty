package com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.navigator.Navigator
import com.homesoftwaretools.toptalproperty.core.navigator.Routes
import com.homesoftwaretools.toptalproperty.core.ui.BaseFragment
import com.homesoftwaretools.toptalproperty.core.ui.BaseViewModel
import com.homesoftwaretools.toptalproperty.core.ui.onClick
import com.homesoftwaretools.toptalproperty.core.ui.visible
import com.homesoftwaretools.toptalproperty.core.utils.NumberFormatter
import com.homesoftwaretools.toptalproperty.core.utils.ResourceProvider
import com.homesoftwaretools.toptalproperty.core.utils.Toaster
import com.homesoftwaretools.toptalproperty.domain.Apartment
import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist.adapter.ApartmentListAdapter
import com.homesoftwaretools.toptalproperty.logd
import com.homesoftwaretools.toptalproperty.repo.UserRepo
import org.koin.android.ext.android.inject
import org.koin.core.inject

class ApartmentListScreen : BaseFragment() {
    override val layoutId = R.layout.property_list_screen

    lateinit var recycler: RecyclerView
    lateinit var adapter: ApartmentListAdapter
    lateinit var fab: FloatingActionButton

    private val fmt: NumberFormatter by inject()
    private val rp: ResourceProvider by inject()
    private val vm: ApartmentListViewModel by scopedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        adapter = ApartmentListAdapter(
            view.context, fmt, rp,
            onClick = vm::openApartment,
            onMapClick = vm::openMap
        )
        recycler.adapter = adapter
        vm.data.onChange(adapter::swapData)
        vm.currentUser.onChange(this::updateUi)
        fab.onClick { vm.createApartment() }
    }

    private fun initView(v: View) {
        recycler = v.findViewById(R.id.recycler)
        fab = v.findViewById(R.id.fab)
    }

    private fun updateUi(user: User) {
        fab.visible = user.role.isCrudApartmentAllowed
    }
}

class ApartmentListViewModel(scopeId: String) : BaseViewModel(scopeId) {

    private val toaster: Toaster by scope.inject()
    private val navigator: Navigator by scope.inject()
    private val useCase: ApartmentListUseCase by inject()
    private val users: UserRepo by inject()

    val data: LiveData<List<Pair<Apartment, User>>> = MutableLiveData()
    val currentUser: LiveData<User> = MutableLiveData()

    init {
        users.getCurrentUser()
            .bindSubscribe(
                onSuccess = (currentUser as MutableLiveData)::postValue,
                onError = toaster::showError
            )

        useCase.getList()
            .bindSubscribe(
                onNext = this::postData,
                onError = toaster::showError
            )
    }

    private fun postData(list: List<Pair<Apartment, User>>) {
        (data as MutableLiveData).postValue(list)
    }

    fun openApartment(id: String) {
        navigator.push(Routes.APARTMENT_EDITOR, mapOf("id" to id))
    }

    fun openMap(apartment: Apartment) {
        navigator.push(Routes.APARTMENT_MAP, mapOf("id" to apartment.id!!))
    }

    fun createApartment() {
        navigator.push(Routes.APARTMENT_EDITOR)
    }


}

