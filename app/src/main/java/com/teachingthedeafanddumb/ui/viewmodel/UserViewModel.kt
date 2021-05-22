package com.teachingthedeafanddumb.ui.viewmodel

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teachingthedeafanddumb.data.model.UserModel
import com.teachingthedeafanddumb.data.repository.UserRepository
import com.teachingthedeafanddumb.other.Constants.KEY_NOT_EXISTS
import com.teachingthedeafanddumb.utils.Resource
import com.teachingthedeafanddumb.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class UserViewModel @ViewModelInject constructor(var repository: UserRepository): ViewModel() {

    val userMutableLiveData: SingleLiveEvent<Resource<UserModel>> = SingleLiveEvent()
    val isUserExistsMutableLiveData: SingleLiveEvent<Resource<UserModel>> = SingleLiveEvent()


    fun registerNewUser(userModel: UserModel, uri : Uri?) =

           viewModelScope.launch {

           userMutableLiveData.postValue(Resource.Loading())
           val userModel = repository.registerUser(userModel, uri)
           userMutableLiveData.postValue(Resource.Success(userModel))

       }

    fun isUserExists(phone: String) =
        viewModelScope.launch {

            val result = repository.isUserExists(phone)

            if (result != null && result.exists()) {

                var user : UserModel?=null

                result.children.forEach{

                    user = it.getValue(UserModel::class.java)!!

                }

                isUserExistsMutableLiveData.postValue(Resource.Success(user))




            } else {

                isUserExistsMutableLiveData.postValue(Resource.Error(KEY_NOT_EXISTS))

            }
        }


}