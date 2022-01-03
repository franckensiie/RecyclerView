package com.pkg.recyclerview.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    @SerialName("token")
    val token: String,
    @SerialName("expire")
    val expire: String
)
