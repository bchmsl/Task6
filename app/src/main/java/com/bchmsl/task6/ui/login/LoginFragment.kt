package com.bchmsl.task6.ui.login

import android.util.Log
import androidx.core.view.isVisible
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bchmsl.task6.databinding.FragmentLoginBinding
import com.bchmsl.task6.datastore.Datastore.REMEMBER_KEY
import com.bchmsl.task6.datastore.Datastore.TOKEN_KEY
import com.bchmsl.task6.datastore.Datastore.dataStore
import com.bchmsl.task6.model.LoginUser
import com.bchmsl.task6.model.LoginUserResponse
import com.bchmsl.task6.model.UserPreference
import com.bchmsl.task6.ui.base.BaseFragment
import com.bchmsl.task6.util.ResponseHandler
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException


class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel: LoginViewModel by viewModels()
    override fun start() {
        binding.etEmail.setText("eve.holt@reqres.in")
        binding.etPassword.setText("cityslicka")
        listeners()
        checkUser()
    }

    private fun listeners() {
        binding.btnSignup.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
        }
    }

    private fun checkUser() {
        viewLifecycleOwner.lifecycleScope.launch{
            getToken().collect{
                Log.wtf("TAG", it.toString())
                if (it.isRemembered){
                    handleRememberedUser(it.token!!)
                }else{
                    loginUser()
                }
            }
        }

    }

    private fun handleRememberedUser(token: String) {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                token
            )
        )
    }

    private fun getToken(): Flow<UserPreference> {
        val readFromDataStore: Flow<UserPreference> = requireContext().dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.d("DataStoreRepository", exception.message.toString())
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                val username = preference[TOKEN_KEY] ?: "HELLO"
                val remember = preference[REMEMBER_KEY] ?: false
                UserPreference(username, remember)

            }
        return readFromDataStore
    }

    private fun loginUser() {
        binding.btnLogin.setOnClickListener {
            viewModel.loginUser(
                LoginUser(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            )
            observe()
        }
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginFlow.collect {
                binding.pbLogin.isVisible = it.isLoading
                when (it) {
                    is ResponseHandler.Success<*> -> handleSuccess(it.data as LoginUserResponse?)
                    is ResponseHandler.Error -> handleError(it.throwable)
                    else -> {}
                }
            }
        }
    }

    private fun handleSuccess(data: LoginUserResponse?) {
        if (binding.cbRememberMe.isChecked) {
            viewLifecycleOwner.lifecycleScope.launch {
                requireContext().dataStore.edit { preferences ->
                    preferences[TOKEN_KEY] = data?.token ?: "Hello World"
                    preferences[REMEMBER_KEY] = binding.cbRememberMe.isChecked
                    Log.wtf("TAG", preferences[TOKEN_KEY])
                }
            }
        }
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment(data?.token.toString()))
    }

    private fun handleError(throwable: Throwable) {
        Snackbar.make(binding.root, throwable.message.toString(), Snackbar.LENGTH_SHORT).show()
    }
}