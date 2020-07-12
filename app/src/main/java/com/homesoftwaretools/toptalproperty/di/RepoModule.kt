package com.homesoftwaretools.toptalproperty.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.homesoftwaretools.toptalproperty.repo.*
import com.homesoftwaretools.toptalproperty.repo.dao.ApartmentDao
import com.homesoftwaretools.toptalproperty.repo.dao.UserDao
import com.homesoftwaretools.toptalproperty.repo.firebase.*
import com.homesoftwaretools.toptalproperty.repo.googlemap.GoogleGeocodeRepo
import org.koin.dsl.module

val repoModule = module {
    factory<UserMapper> { FireStoreUserMapper() }
    factory<ApartmentMapper> { FireStoreApartmentMapper(dt = get()) }
    factory<FirebaseFirestore> { Firebase.firestore }

    single<UserDao> { FireStoreUserDao(db = get(), mapper = get()) }
    single<ApartmentDao> { FireStoreApartmentDao(db = get(), mapper = get()) }

    single<AuthRepo> { FirebaseAuthRepo() }
    single<UserRepo> { UserRepoImpl(userDao = get(), authRepo = get()) }
    single<ApartmentRepo> { ApartmentRepoImpl(dao = get(), dt = get()) }
    single<GeocodeRepo> { GoogleGeocodeRepo() }
}