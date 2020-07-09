package com.homesoftwaretools.toptalproperty.features.editor

import com.homesoftwaretools.toptalproperty.domain.Apartment
import com.homesoftwaretools.toptalproperty.repo.ApartmentRepo
import com.homesoftwaretools.toptalproperty.repo.UserRepo
import io.reactivex.Single

interface ApartmentEditorUseCase {
    fun save(apartment: Apartment): Single<Apartment>
    fun load(id: String): Single<Apartment>
}


class ApartmentEditorUseCaseImpl(
    private val apartmentRepo: ApartmentRepo,
    private val userRepo: UserRepo
) : ApartmentEditorUseCase {

    override fun save(apartment: Apartment): Single<Apartment> =
        userRepo.getCurrentUser()
            .flatMap { user -> apartmentRepo.save(apartment, user) }

    override fun load(id: String): Single<Apartment> =
        apartmentRepo.get(id)
}