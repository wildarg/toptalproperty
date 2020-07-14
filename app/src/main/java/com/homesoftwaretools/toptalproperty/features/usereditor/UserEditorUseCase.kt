package com.homesoftwaretools.toptalproperty.features.usereditor

import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.repo.AuthRepo
import com.homesoftwaretools.toptalproperty.repo.UserRepo
import io.reactivex.Completable
import io.reactivex.Single

interface UserEditorUseCase {
    fun getUser(authId: String): Single<User>
    fun createUser(user: User, password: String): Single<User>
    fun saveUser(user: User): Single<User>
    fun deleteUser(authId: String): Completable
}

class UserEditorUseCaseImpl(
    private val authRepo: AuthRepo,
    private val userRepo: UserRepo
) : UserEditorUseCase {

    override fun getUser(authId: String): Single<User> =
        userRepo.getUser(authId)

    override fun createUser(user: User, password: String): Single<User> =
        authRepo.signUp(user.email, password)
            .map { uid -> user.copy(authId = uid) }
            .flatMap(userRepo::registerUser)

    override fun saveUser(user: User): Single<User> =
        userRepo.registerUser(user)

    override fun deleteUser(authId: String): Completable =
        userRepo.unregisterUser(authId)
            .toCompletable()
}