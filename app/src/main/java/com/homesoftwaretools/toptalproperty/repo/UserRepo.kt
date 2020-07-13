package com.homesoftwaretools.toptalproperty.repo

import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.domain.UserRole
import com.homesoftwaretools.toptalproperty.repo.dao.UserDao
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepo {
    fun getUser(authId: String): Single<User>
    fun getCurrentUser(): Single<User>
    fun registerUser(role: UserRole, name: String): Single<User>
}

class UserRepoImpl(
    private val userDao: UserDao,
    private val authRepo: AuthRepo
) : UserRepo {

    override fun getUser(authId: String): Single<User> =
        userDao.get(authId)

    override fun getCurrentUser(): Single<User> =
        authRepo.currentAuthId()
            .flatMap(userDao::get)

    override fun registerUser(role: UserRole, name: String): Single<User> =
        authRepo.currentUidAndEmail()
            .map { (uid, email) ->
                User(
                    authId = uid,
                    name = name,
                    email = email,
                    role = role
                )
            }
            .flatMap(userDao::save)

}