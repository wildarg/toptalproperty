package com.homesoftwaretools.toptalproperty.repo

import com.homesoftwaretools.toptalproperty.domain.Filter
import io.reactivex.Completable
import io.reactivex.Observable

interface FilterRepo {
    fun observeFilter(): Observable<Filter>
    fun setFilter(filter: Filter): Completable
}