package com.homesoftwaretools.toptalproperty.domain

sealed class UserRole(val type: Int) {
    object Admin : UserRole(0)
    object Realtor : UserRole(1)
    object Client : UserRole(2)

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
)