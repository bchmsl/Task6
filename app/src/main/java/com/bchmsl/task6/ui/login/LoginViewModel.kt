package com.bchmsl.task6.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bchmsl.task6.model.LoginUser
import com.bchmsl.task6.network.RetrofitProvider
import com.bchmsl.task6.util.ResponseHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.log

class LoginViewModel : ViewModel() {

    private val _loginFlow = MutableStateFlow<ResponseHandler>(ResponseHandler.Loading())
    val loginFlow get() = _loginFlow.asStateFlow()

    fun loginUser(loginUser: LoginUser) {
        viewModelScope.launch {
            val response = RetrofitProvider.getApiClient().loginUser(loginUser)
            if (response.isSuccessful && response.body() != null) {
                _loginFlow.emit(ResponseHandler.Success(response.body()))
            } else {
                _loginFlow.emit(ResponseHandler.Error(Throwable(response.message())))
            }

        }
    }
}