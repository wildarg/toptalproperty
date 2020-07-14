package com.homesoftwaretools.toptalproperty.features.welcome.signup

import com.homesoftwaretools.toptalproperty.repo.AuthRepo
import io.reactivex.Completable

interface SignUpUseCase {
    fun signUp(email: String, password: String): Completable
}


class SignUpUseCaseImpl(
    private val authRepo: AuthRepo
) : SignUpUseCase {

    override fun signUp(email: String, password: String): Completable =
        authRepo.signUp(email, password)
            .toCompletable()

}