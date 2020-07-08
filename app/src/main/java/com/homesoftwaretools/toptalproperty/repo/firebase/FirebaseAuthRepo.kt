package com.homesoftwaretools.toptalproperty.repo.firebase

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.homesoftwaretools.toptalproperty.repo.AuthRepo
import com.homesoftwaretools.toptalproperty.repo.UserCancelException
import com.homesoftwaretools.toptalproperty.repo.UserNotLoggedIn
import io.reactivex.Completable
import io.reactivex.Single

class FirebaseAuthRepo : AuthRepo {

    private val auth = FirebaseAuth.getInstance()

    override fun logIn(email: String, password: String): Completable = Completable.create { e ->
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { t ->
                when {
                    t.isSuccessful -> e.onComplete()
                    else -> e.onError(t.exception ?: FirebaseException("Unknown Error"))
                }
            }
            .addOnCanceledListener {
                e.onError(UserCancelException())
            }
    }

    override fun currentAuthId(): Single<String> = Single.create { s ->
        auth.currentUser?.let {
            s.onSuccess(it.uid)
        } ?: s.onError( UserNotLoggedIn() )
    }

    override fun signOff(): Completable = Completable.create { c ->
        auth.signOut()
        c.onComplete()
    }

}