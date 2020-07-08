package com.homesoftwaretools.toptalproperty.features.splash

import com.homesoftwaretools.toptalproperty.repo.AuthRepo
import com.homesoftwaretools.toptalproperty.repo.UserRepo
import io.reactivex.Single

interface SplashUseCase {
    fun isLoggedIn(): Single<Boolean>
}

class SplashUseCaseImpl(
    private val authRepo: AuthRepo,
    private val userRepo: UserRepo
) : SplashUseCase {

    override fun isLoggedIn(): Single<Boolean> =
        authRepo.currentAuthId()
            .flatMap(userRepo::getUser)
            .map { true }
            .onErrorReturnItem(false)

}