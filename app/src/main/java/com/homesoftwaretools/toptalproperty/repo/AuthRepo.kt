package com.homesoftwaretools.toptalproperty.repo

import io.reactivex.Completable
import io.reactivex.Single

interface AuthRepo {
    fun logIn(email: String, password: String): Single<String>
    fun signUp(email: String, password: String): Completable
    fun currentAuthId(): Single<String>
    fun currentUidAndEmail(): Single<Pair<String, String>>
    fun signOff(): Completable
}

class UserCancelException : Exception()
class UserNotLoggedIn : Exception()