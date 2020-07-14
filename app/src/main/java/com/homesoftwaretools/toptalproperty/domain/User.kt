package com.homesoftwaretools.toptalproperty.domain

sealed class UserRole(val type: Int, val isCrudApartmentAllowed: Boolean = true) {
    object Admin : UserRole(
        type = 0
    )
    object Realtor : UserRole(
        type = 1
    )
    object Client : UserRole(
        type = 2,
        isCrudApartmentAllowed = false
    )

    companion object {
        fun getRole(type: Int): UserRole {
            return when (type) {
                Admin.type  -> Admin
                Realtor.type -> Realtor
                Client.type -> Client
                else -> throw Exception("Unknown role type $type")
            }
        }
    }
}

data class User(
    val id: String? = null,
    val authId: String,
    val name: String,
    val email: String,
    val role: UserRole
) {
    companion object {
        val EMPTY = User(
            authId = "",
            name = "",
            email = "",
            role = UserRole.Client
        )
    }
}