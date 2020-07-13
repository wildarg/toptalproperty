package com.homesoftwaretools.toptalproperty.features.welcome.register

import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.domain.UserRole
import com.homesoftwaretools.toptalproperty.repo.AuthRepo
import com.homesoftwaretools.toptalproperty.repo.UserRepo
import io.reactivex.Completable
import io.reactivex.Single

interface RegisterUseCase {
    fun registerNewUser(role: UserRole, name: String): Single<User>
    fun signOut(): Completable
}


class RegisterUseCaseImpl(
    private val authRepo: AuthRepo,
    private val userRepo: UserRepo
) : RegisterUseCase {

    override fun registerNewUser(role: UserRole, name: String): Single<User> =
        userRepo.registerUser(role, name)

    override fun signOut(): Completable =
        authRepo.signOff()

}