package com.homesoftwaretools.toptalproperty.features.welcome.login

import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.repo.AuthRepo
import com.homesoftwaretools.toptalproperty.repo.UserRepo
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

interface LoginUseCase {
    fun login(email: String, password: String): Single<User>
}

class LoginUseCaseImpl(
    private val authRepo: AuthRepo,
    private val userRepo: UserRepo
) : LoginUseCase {

    override fun login(email: String, password: String): Single<User> =
        authRepo.logIn(email, password)
            .flatMap(userRepo::getUser)

}