package com.homesoftwaretools.toptalproperty.repo.prefs

import com.homesoftwaretools.toptalproperty.domain.Filter
import com.homesoftwaretools.toptalproperty.repo.FilterRepo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class SharedPrefsFilterRepo(
    private val prefs: PrefsRepo
) : FilterRepo {

    private val subj = BehaviorSubject.createDefault(Filter.EMPTY)

    override fun observeFilter(): Observable<Filter> =
        subj.doOnSubscribe { updateFromPrefs() }
            .distinctUntilChanged()

    private fun updateFromPrefs() {
        subj.onNext(getFilter())
    }

    override fun setFilter(filter: Filter): Completable {
        return Completable.create { c ->
            saveFilter(filter)
            subj.onNext(filter)
            c.onComplete()
        }
    }

    private fun saveFilter(filter: Filter) {
        prefs.setNum(MIN_PRICE, filter.minPrice?.toFloat() ?: 0f)
        prefs.setNum(MAX_PRICE, filter.maxPrice?.toFloat() ?: 0f)
        prefs.setNum(MIN_AREA, filter.minArea?.toFloat() ?: 0f)
        prefs.setNum(MAX_AREA, filter.maxArea?.toFloat() ?: 0f)
        prefs.setNum(MIN_ROOMS, filter.minRooms?.toFloat() ?: 0f)
        prefs.setNum(MAX_ROOMS, filter.maxRooms?.toFloat() ?: 0f)
    }

    private fun getFilter(): Filter = Filter(
        minArea = prefs.getNum(MIN_AREA)?.toDouble(),
        maxArea = prefs.getNum(MAX_AREA)?.toDouble(),
        minPrice = prefs.getNum(MIN_PRICE)?.toDouble(),
        maxPrice = prefs.getNum(MAX_PRICE)?.toDouble(),
        minRooms = prefs.getNum(MIN_ROOMS)?.toInt(),
        maxRooms = prefs.getNum(MAX_ROOMS)?.toInt()
    )

    companion object {
        private const val MIN_PRICE = "min_price"
        private const val MAX_PRICE = "max_price"
        private const val MIN_AREA = "min_area"
        private const val MAX_AREA = "max_area"
        private const val MIN_ROOMS = "min_rooms"
        private const val MAX_ROOMS = "max_rooms"
    }
}