package com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist

import com.homesoftwaretools.toptalproperty.domain.Apartment
import com.homesoftwaretools.toptalproperty.domain.Filter
import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.repo.ApartmentRepo
import com.homesoftwaretools.toptalproperty.repo.UserRepo
import io.reactivex.Observable
import io.reactivex.Single

interface ApartmentListUseCase {
    fun getList(filter: Filter = Filter.EMPTY): Observable<List<Pair<Apartment, User>>>
}


class ApartmentListUseCaseImpl(
    private val apartRepo: ApartmentRepo,
    private val userRepo: UserRepo
) : ApartmentListUseCase {

    override fun getList(filter: Filter): Observable<List<Pair<Apartment, User>>> =
        apartRepo.getAll()
            .flatMapSingle(this::connectWithUsers)

    private fun connectWithUsers(list: List<Apartment>): Single<List<Pair<Apartment, User>>> {
        return Observable.fromIterable(list)
            .flatMapSingle { ap ->
                userRepo.getUser(ap.realtorId!!)
                    .map { Pair(ap, it) }
            }.toList()

    }
}