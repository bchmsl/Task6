package com.bchmsl.task6.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bchmsl.task6.model.LoginUser
import com.bchmsl.task6.model.SignupUser
import com.bchmsl.task6.network.RetrofitProvider
import com.bchmsl.task6.util.ResponseHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignupViewModel: ViewModel() {

    private val _signupFlow = MutableStateFlow<ResponseHandler>(ResponseHandler.Loading())
    val signupFlow get() = _signupFlow.asStateFlow()

    fun signUpUser(signupUser: SignupUser) {
        viewModelScope.launch {
            try {
                val response = RetrofitProvider.getApiClient().signUpUser(signupUser)
                if (response.isSuccessful && response.body()!= null){
                    _signupFlow.emit(ResponseHandler.Success(response.body()))
                }else{
                    _signupFlow.emit(ResponseHandler.Error(Throwable(response.message())))
                }
            }catch (e: Throwable){
                _signupFlow.emit(ResponseHandler.Error(e))
            }
        }
    }
}