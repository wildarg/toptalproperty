package com.homesoftwaretools.toptalproperty.repo.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.homesoftwaretools.toptalproperty.domain.Apartment
import com.homesoftwaretools.toptalproperty.domain.Filter
import com.homesoftwaretools.toptalproperty.logd
import com.homesoftwaretools.toptalproperty.repo.dao.ApartmentDao
import com.homesoftwaretools.toptalproperty.repo.dao.ApartmentNotFoundException
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class FireStoreApartmentDao(
    private val db: FirebaseFirestore,
    private val mapper: ApartmentMapper
) : ApartmentDao {

    private val collection
        get() = db.collection(APARTMENTS)

    private fun toApartment(doc: DocumentSnapshot?): Apartment? =
        doc?.data?.apply { put(FireStoreUserDao.ID, doc.id) }
            ?.let(mapper::fromMap)

    private fun Query.inRange(field: String, min: Any?, max: Any?): Query {
        val query = min?.let { whereGreaterThanOrEqualTo(field, it) } ?: this
        return max?.let { query.whereLessThanOrEqualTo(field, it) } ?: query
    }

    override fun getAll(filter: Filter): Observable<List<Apartment>> = Observable.create { o ->
        val listener = collection
            .inRange(AREA, filter.minArea, filter.maxArea)
            .inRange(PRICE, filter.minPrice, filter.maxPrice)
            .inRange(ROOMS, filter.minRooms, filter.maxRooms)
            .addSnapshotListener { snapshot, e ->
                if (o.isDisposed) return@addSnapshotListener
                when {
                    e != null        -> o.onError(e)
                    snapshot != null -> o.onNext(snapshot.documents.mapNotNull(this::toApartment))
                }
            }
        o.setCancellable { listener.remove() }
    }

    override fun save(apartment: Apartment): Single<Apartment> = Single.create { s ->
        val data = mapper.toMap(apartment)
        val task: Task<*> = apartment.id?.let { id -> collection.document(id).update(data) }
            ?: collection.add(data)
        task.addOnSuccessListener {
            if (it is DocumentReference?)
                s.onSuccess(apartment.copy(id = it?.id))
            else
                s.onSuccess(apartment)
        }
            .addOnFailureListener(s::onError)
    }

    override fun get(id: String): Single<Apartment> = Single.create { s ->
        collection.document(id)
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot
                    ?.let(this::toApartment)
                    ?.let(s::onSuccess)
                    ?: s.onError(ApartmentNotFoundException(id))
            }
            .addOnFailureListener(s::onError)
    }

    override fun delete(id: String): Completable = Completable.create { c ->
        collection.document(id).delete()
            .addOnSuccessListener { c.onComplete() }
            .addOnFailureListener(c::onError)
    }

    companion object {
        const val APARTMENTS = "apartments"
        const val ID = "id"
        const val CREATED = "created"
        const val REALTOR_ID = "realtorId"
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val AREA = "area"
        const val PRICE = "price"
        const val ROOMS = "rooms"
        const val LOCATION = "location"
        const val RENTED = "rented"
    }

}