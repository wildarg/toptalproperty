package com.homesoftwaretools.toptalproperty.features.usercard

import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.repo.AuthRepo
import com.homesoftwaretools.toptalproperty.repo.UserRepo
import io.reactivex.Completable
import io.reactivex.Single

interface UserCardUseCase {
    fun getCurrentUser(): Single<User>
    fun signOff(): Completable
}

class UserCardUseCaseImpl(
    private val authRepo: AuthRepo,
    private val userRepo: UserRepo
) : UserCardUseCase {

    override fun getCurrentUser(): Single<User> =
        userRepo.getCurrentUser()

    override fun signOff(): Completable =
        authRepo.signOff()

}