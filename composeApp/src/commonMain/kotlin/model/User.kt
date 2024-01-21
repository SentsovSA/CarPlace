package model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userEmail: String,
    val userID: Int,
    val userName: String,
    val userPhone: String,
    val userPassword: String
)