package com.homesoftwaretools.toptalproperty.repo.firebase

import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.domain.UserRole

interface UserMapper {
    fun fromMap(map: Map<String?, Any?>?): User?
    fun toMap(user: User): Map<String, Any>
}


class FireStoreUserMapper : UserMapper {

    private fun getRole(type: Long): UserRole = when (type) {
        0L   -> UserRole.Admin
        1L   -> UserRole.Client
        else    -> UserRole.Realtor
    }

    private fun getType(role: UserRole): Long = when (role) {
        UserRole.Admin -> 0
        UserRole.Client -> 1
        UserRole.Realtor -> 2
    }

    override fun fromMap(map: Map<String?, Any?>?): User? = if (map != null) User(
        id = map[FireStoreUserDao.ID] as? String,
        authId = map[FireStoreUserDao.AUTH_ID] as? String ?: "",
        name = map[FireStoreUserDao.NAME] as? String ?: "",
        email = map[FireStoreUserDao.EMAIL] as? String ?: "",
        role = getRole(map[FireStoreUserDao.TYPE] as? Long ?: 1)
    ) else null

    override fun toMap(user: User): HashMap<String, Any> = hashMapOf(
        FireStoreUserDao.AUTH_ID to user.authId,
        FireStoreUserDao.NAME to user.name,
        FireStoreUserDao.EMAIL to user.email,
        FireStoreUserDao.TYPE to getType(user.role)
    )

}