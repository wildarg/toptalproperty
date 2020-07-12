package com.homesoftwaretools.toptalproperty.features.editor

import com.homesoftwaretools.toptalproperty.domain.Apartment
import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.repo.ApartmentRepo
import com.homesoftwaretools.toptalproperty.repo.UserRepo
import io.reactivex.Single

interface ApartmentEditorUseCase {
    fun save(apartment: Apartment): Single<Apartment>
    fun load(id: String): Single<Pair<Apartment, User>>
}


class ApartmentEditorUseCaseImpl(
    private val apartmentRepo: ApartmentRepo,
    private val userRepo: UserRepo
) : ApartmentEditorUseCase {

    override fun save(apartment: Apartment): Single<Apartment> =
        userRepo.getCurrentUser()
            .flatMap { user -> apartmentRepo.save(apartment, user) }

    override fun load(id: String): Single<Pair<Apartment, User>> =
        apartmentRepo.get(id)
            .flatMap { ap ->
                userRepo.getUser(ap.realtorId!!).map { Pair(ap, it) }
            }
}