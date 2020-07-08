package com.homesoftwaretools.toptalproperty.features.welcome.login

import com.homesoftwaretools.toptalproperty.repo.AuthRepo
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

interface LoginUseCase {
    fun login(email: String, password: String): Single<String>
}

class LoginUseCaseImpl : LoginUseCase, KoinComponent {

    private val authRepo: AuthRepo by inject()

    override fun login(email: String, password: String): Single<String> =
        authRepo.logIn(email, password)
            .toSingle { "Unknown Route" }

}