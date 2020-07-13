package com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist

import com.homesoftwaretools.toptalproperty.domain.Apartment
import com.homesoftwaretools.toptalproperty.domain.Filter
import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.logd
import com.homesoftwaretools.toptalproperty.repo.ApartmentRepo
import com.homesoftwaretools.toptalproperty.repo.FilterRepo
import com.homesoftwaretools.toptalproperty.repo.UserRepo
import io.reactivex.Observable
import io.reactivex.Single

interface ApartmentListUseCase {
    fun getList(): Observable<List<Pair<Apartment, User>>>
}


class ApartmentListUseCaseImpl(
    private val apartRepo: ApartmentRepo,
    private val userRepo: UserRepo,
    private val filterRepo: FilterRepo
) : ApartmentListUseCase {

    override fun getList(): Observable<List<Pair<Apartment, User>>> =
        filterRepo.observeFilter()
            .switchMap { apartRepo.getAll(it) }
            .flatMapSingle(this::connectWithUsers)

    private fun connectWithUsers(list: List<Apartment>): Single<List<Pair<Apartment, User>>> {
        return Observable.fromIterable(list)
            .flatMapSingle { ap ->
                userRepo.getUser(ap.realtorId!!)
                    .map { Pair(ap, it) }
            }.toList()

    }
}