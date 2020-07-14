package com.homesoftwaretools.toptalproperty.repo.dao

import com.homesoftwaretools.toptalproperty.domain.User
import io.reactivex.Observable
import io.reactivex.Single

interface UserDao {
    fun getAll(): Observable<List<User>>
    fun get(authId: String): Single<User>
    fun save(user: User): Single<User>
    fun delete(user: User): Single<User>
}

class UserNotFoundException(authId: String) : Exception("User $authId not found!")