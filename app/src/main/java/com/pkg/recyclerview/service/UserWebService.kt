package com.pkg.recyclerview.service

import com.pkg.recyclerview.model.UserInfo
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserWebService {
    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>

    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>;

    @PATCH("users")
    suspend fun update(@Body user: UserInfo): Response<UserInfo>
}