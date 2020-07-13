package com.homesoftwaretools.toptalproperty.features.filter

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.navigator.Navigator
import com.homesoftwaretools.toptalproperty.core.ui.BaseFragment
import com.homesoftwaretools.toptalproperty.core.ui.BaseViewModel
import com.homesoftwaretools.toptalproperty.core.ui.onClick
import com.homesoftwaretools.toptalproperty.core.utils.NumberFormatter
import com.homesoftwaretools.toptalproperty.core.utils.Toaster
import com.homesoftwaretools.toptalproperty.domain.Filter
import com.homesoftwaretools.toptalproperty.repo.FilterRepo
import org.koin.android.ext.android.inject
import org.koin.core.inject

class FilterScreen : BaseFragment() {
    override val layoutId = R.layout.filter_screen

    private lateinit var minPrice: EditText
    private lateinit var maxPrice: EditText
    private lateinit var minArea: EditText
    private lateinit var maxArea: EditText
    private lateinit var minRooms: EditText
    private lateinit var maxRooms: EditText
    private lateinit var applyButton: View

    private val vm: FilterViewModel by scopedViewModel()
    private val fmt: NumberFormatter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)

        vm.filter.onChange(this::populate)
        applyButton.onClick {
            vm.setFilter(it, collect())
        }
    }

    private fun initView(v: View) {
        minPrice = v.findViewById(R.id.min_price_editor)
        maxPrice = v.findViewById(R.id.max_price_editor)
        minArea = v.findViewById(R.id.min_area_editor)
        maxArea = v.findViewById(R.id.max_area_editor)
        minRooms = v.findViewById(R.id.min_rooms_editor)
        maxRooms = v.findViewById(R.id.max_rooms_editor)
        applyButton = v.findViewById(R.id.apply_filter)
    }

    private fun populate(filter: Filter) {
        minPrice.setText(filter.minPrice?.let(fmt::formatNum))
        maxPrice.setText(filter.maxPrice?.let(fmt::formatNum))
        minArea.setText(filter.minArea?.let(fmt::formatNum))
        maxArea.setText(filter.maxArea?.let(fmt::formatNum))
        minRooms.setText(filter.minRooms?.let(fmt::formatInt))
        maxRooms.setText(filter.maxRooms?.let(fmt::formatInt))
    }

    private fun collect(): Filter {
        return Filter(
            minPrice = fmt.parseNumOrNull(minPrice.text.toString()),
            maxPrice = fmt.parseNumOrNull(maxPrice.text.toString()),
            minArea = fmt.parseNumOrNull(minArea.text.toString()),
            maxArea = fmt.parseNumOrNull(maxArea.text.toString()),
            minRooms = fmt.parseIntOrNull(minRooms.text.toString()),
            maxRooms = fmt.parseIntOrNull(maxRooms.text.toString())
        )
    }
}


class FilterViewModel(scopeId: String) : BaseViewModel(scopeId) {

    private val toaster: Toaster by scope.inject()
    private val navigator: Navigator by scope.inject()
    private val repo: FilterRepo by inject()

    val filter: LiveData<Filter> = MutableLiveData()

    init {
        repo.observeFilter()
            .bindSubscribe(
                onNext = (filter as MutableLiveData)::postValue,
                onError = toaster::showError
            )
    }

    fun setFilter(view: View, filter: Filter) {
        navigator.hideSoftKeyboard(view)
        repo.setFilter(filter)
            .bindSubscribe(
                onComplete = { navigator.hideDrawer() },
                onError = toaster::showError
            )
    }
}