package com.homesoftwaretools.toptalproperty.repo.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.repo.dao.UserDao
import com.homesoftwaretools.toptalproperty.repo.dao.UserNotFoundException
import io.reactivex.Observable
import io.reactivex.Single

class FireStoreUserDao(
    private val db: FirebaseFirestore,
    private val mapper: UserMapper
) : UserDao {

    private val collection
        get() = db.collection(USERS)

    private fun toUser(doc: DocumentSnapshot?): User? =
        doc?.data?.apply { put(ID, doc.id) }
            ?.let(mapper::fromMap)

    override fun getAll(): Observable<List<User>> = Observable.create { o ->
        val listener = collection
            .addSnapshotListener { snapshot, e ->
                when {
                    e != null   -> o.onError(e)
                    snapshot != null    -> snapshot.documents
                        .mapNotNull(this::toUser)
                        .let(o::onNext)
                }
            }
        o.setCancellable { listener.remove() }
    }

    override fun get(authId: String): Single<User> = Single.create { s ->
        collection.whereEqualTo(AUTH_ID, authId).get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents
                    .mapNotNull(this::toUser)
                if (list.isNotEmpty())
                    s.onSuccess(list.first())
                else
                    s.onError(UserNotFoundException(authId))
            }
            .addOnFailureListener(s::onError)
    }

    override fun save(user: User): Single<User> = Single.create { s ->
        val data = mapper.toMap(user)
        val task: Task<*> = user.id?.let { id -> collection.document(id).update(data) }
            ?: collection.add(data)
        task.addOnSuccessListener {
                if (it is DocumentReference?)
                    s.onSuccess(user.copy(id = it?.id))
                else
                    s.onSuccess(user)
            }
            .addOnFailureListener(s::onError)
    }

    override fun delete(user: User): Single<User> = Single.create { s ->
        user.id?.let { id ->
            collection.document(id).delete()
                .addOnSuccessListener { s.onSuccess(user) }
                .addOnFailureListener(s::onError)
        } ?: s.onError(UserNotFoundException("null"))
    }

    companion object {
        const val USERS: String = "users"
        const val ID: String = "id"
        const val AUTH_ID: String = "authId"
        const val EMAIL: String = "email"
        const val NAME: String = "name"
        const val TYPE: String = "type"
    }
}