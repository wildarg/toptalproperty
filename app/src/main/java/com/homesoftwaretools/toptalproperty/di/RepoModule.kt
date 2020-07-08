package com.homesoftwaretools.toptalproperty.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.homesoftwaretools.toptalproperty.repo.AuthRepo
import com.homesoftwaretools.toptalproperty.repo.UserRepo
import com.homesoftwaretools.toptalproperty.repo.UserRepoImpl
import com.homesoftwaretools.toptalproperty.repo.dao.ApartmentDao
import com.homesoftwaretools.toptalproperty.repo.dao.UserDao
import com.homesoftwaretools.toptalproperty.repo.firebase.*
import org.koin.dsl.module

val repoModule = module {
    factory<UserMapper> { FireStoreUserMapper() }
    factory<ApartmentMapper> { FireStoreApartmentMapper(dt = get()) }
    factory<FirebaseFirestore> { Firebase.firestore }

    single<UserDao> { FireStoreUserDao(db = get(), mapper = get()) }
    single<ApartmentDao> { FireStoreApartmentDao(db = get(), mapper = get()) }

    single<AuthRepo> { FirebaseAuthRepo() }
    single<UserRepo> { UserRepoImpl(userDao = get(), authRepo = get()) }
}