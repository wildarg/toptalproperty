package com.homesoftwaretools.toptalproperty.repo.firebase

import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.domain.UserRole

interface UserMapper {
    fun fromMap(map: Map<String?, Any?>?): User?
    fun toMap(user: User): Map<String, Any>
}


class FireStoreUserMapper : UserMapper {

    override fun fromMap(map: Map<String?, Any?>?): User? = if (map != null) User(
        id = map[FireStoreUserDao.ID] as? String,
        authId = map[FireStoreUserDao.AUTH_ID] as? String ?: "",
        name = map[FireStoreUserDao.NAME] as? String ?: "",
        email = map[FireStoreUserDao.EMAIL] as? String ?: "",
        role = (map[FireStoreUserDao.TYPE] as? Long)?.toInt()
            ?.let { UserRole.getRole(it) }
            ?: UserRole.Client
    ) else null

    override fun toMap(user: User): HashMap<String, Any> = hashMapOf(
        FireStoreUserDao.AUTH_ID to user.authId,
        FireStoreUserDao.NAME to user.name,
        FireStoreUserDao.EMAIL to user.email,
        FireStoreUserDao.TYPE to user.role.type
    )

}