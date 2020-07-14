package com.homesoftwaretools.toptalproperty.features.userlist

import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.repo.UserRepo
import io.reactivex.Observable

interface UserListUseCase {
    fun getUsers(): Observable<List<User>>
}


class UserListUseCaseImpl(
    private val userRepo: UserRepo
) : UserListUseCase {

    override fun getUsers(): Observable<List<User>> =
        userRepo.getAll()

}