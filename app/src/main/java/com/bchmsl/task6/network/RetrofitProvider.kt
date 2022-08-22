package com.bchmsl.task6.network

import com.bchmsl.task6.model.LoginUser
import com.bchmsl.task6.model.LoginUserResponse
import com.bchmsl.task6.model.SignupUser
import com.bchmsl.task6.model.SignupUserResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object RetrofitProvider {
    private const val BASE_URL = "https://reqres.in/"
    private val retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getApiClient(): ApiClient = retrofit.create(ApiClient::class.java)
}

interface ApiClient {

    @POST("api/login")
    suspend fun loginUser(@Body loginUser: LoginUser): Response<LoginUserResponse>

    @POST("api/register")
    suspend fun signUpUser(@Body signupUser: SignupUser): Response<SignupUserResponse>
}