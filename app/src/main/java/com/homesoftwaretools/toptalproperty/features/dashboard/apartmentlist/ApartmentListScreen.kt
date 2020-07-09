package com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.navigator.Navigator
import com.homesoftwaretools.toptalproperty.core.ui.BaseFragment
import com.homesoftwaretools.toptalproperty.core.ui.BaseViewModel
import com.homesoftwaretools.toptalproperty.core.utils.NumberFormatter
import com.homesoftwaretools.toptalproperty.core.utils.Toaster
import com.homesoftwaretools.toptalproperty.domain.Apartment
import com.homesoftwaretools.toptalproperty.domain.Location
import com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist.adapter.ApartmentListAdapter
import com.homesoftwaretools.toptalproperty.repo.ApartmentRepo
import kotlinx.android.synthetic.main.apartment_card.*
import org.koin.android.ext.android.inject
import org.koin.core.inject

class ApartmentListScreen : BaseFragment() {
    override val layoutId = R.layout.property_list_screen

    lateinit var recycler: RecyclerView
    lateinit var adapter: ApartmentListAdapter

    private val fmt: NumberFormatter by inject()
    private val vm: ApartmentListViewModel by scopedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        adapter = ApartmentListAdapter(view.context, fmt)
        recycler.adapter = adapter
        vm.data.onChange(adapter::swapData)
    }

    private fun initView(v: View) {
        recycler = v.findViewById(R.id.recycler)
    }
}

class ApartmentListViewModel(scopeId: String) : BaseViewModel(scopeId) {

    private val toaster: Toaster by scope.inject()
    private val navigator: Navigator by scope.inject()
    private val repo: ApartmentRepo by inject()

    val data: LiveData<List<Apartment>> = MutableLiveData()

    init {
        repo.getAll()
            .bindSubscribe(
                onNext = this::postData,
                onError = toaster::showError
            )
    }

    private fun postData(list: List<Apartment>) {
        (data as MutableLiveData).postValue(list)
    }

}

