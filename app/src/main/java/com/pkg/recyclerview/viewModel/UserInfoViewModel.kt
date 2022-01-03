package com.pkg.recyclerview.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkg.recyclerview.data.TasksRepository
import com.pkg.recyclerview.data.UserInfoRepository
import com.pkg.recyclerview.model.Task
import com.pkg.recyclerview.model.UserInfo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserInfoViewModel : ViewModel() {
    private val repo = UserInfoRepository();

    suspend fun getInfo(): UserInfo? {
        return repo.getInfo();
    }

    fun updateAvatar(bytes: ByteArray) {
        viewModelScope.launch {
            repo.uploadAvatar(bytes);
        }
    }

    fun updateData(user: UserInfo) {
        viewModelScope.launch {
            repo.updateData(user);
        }
    }
}