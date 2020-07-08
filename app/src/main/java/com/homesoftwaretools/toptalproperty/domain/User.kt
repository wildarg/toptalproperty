package com.homesoftwaretools.toptalproperty.domain

sealed class UserRole {
    object Admin : UserRole()
    object Client : UserRole()
    object Realtor : UserRole()
}

data class User(
    val id: String?,
    val authId: String,
    val name: String,
    val email: String,
    val role: UserRole
)