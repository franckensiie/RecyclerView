package com.pkg.recyclerview.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterForm(
    @SerialName("firstname")
    val firstname: String,
    @SerialName("lastname")
    val name: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("password_confirmation")
    val password_confirmation: String
)
