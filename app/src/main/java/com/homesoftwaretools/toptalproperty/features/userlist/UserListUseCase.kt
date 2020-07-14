package com.homesoftwaretools.toptalproperty.features.userlist

import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.repo.AuthRepo
import com.homesoftwaretools.toptalproperty.repo.UserRepo
import io.reactivex.Observable
import io.reactivex.Single

interface UserListUseCase {
    fun getUsers(): Observable<List<User>>
    fun createUser(user: User, password: String): Single<User>
}


class UserListUseCaseImpl(
    private val userRepo: UserRepo,
    private val authRepo: AuthRepo
) : UserListUseCase {

    override fun getUsers(): Observable<List<User>> =
        userRepo.getAll()

    override fun createUser(user: User, password: String): Single<User> =
        authRepo.signUp(user.email, password)
            .map { uid -> user.copy(authId = uid) }
            .flatMap(userRepo::registerUser)

}