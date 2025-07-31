package com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen

import androidx.lifecycle.ViewModel
import com.yuvrajsinghgmx.shopsmart.modelclass.User
import com.yuvrajsinghgmx.shopsmart.modelclass.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val repository: Repository
): ViewModel(){

    fun getUserData(): User{
        return repository.getUserData()
    }

}