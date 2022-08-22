package com.bchmsl.task6.ui.signup

import android.util.Log
import androidx.core.view.isVisible
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bchmsl.task6.databinding.FragmentSignupBinding
import com.bchmsl.task6.datastore.Datastore
import com.bchmsl.task6.datastore.Datastore.dataStore
import com.bchmsl.task6.model.SignupUser
import com.bchmsl.task6.model.SignupUserResponse
import com.bchmsl.task6.ui.base.BaseFragment
import com.bchmsl.task6.ui.login.LoginFragmentDirections
import com.bchmsl.task6.util.ResponseHandler
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class SignupFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {
    private val viewModel: SignupViewModel by viewModels()
    override fun start() {
        signUpUser()
    }
    private fun signUpUser() {
        binding.btnSignup.setOnClickListener {
            viewModel.signUpUser(
                SignupUser(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            )
            observe()
        }
    }
    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signupFlow.collect {
                binding.pbSignup.isVisible = it.isLoading
                when (it) {
                    is ResponseHandler.Success<*> -> handleSuccess(it.data as SignupUserResponse?)
                    is ResponseHandler.Error -> handleError(it.throwable)
                    else -> {}
                }
            }
        }
    }

    private fun handleSuccess(data: SignupUserResponse?) {
        if (binding.cbRememberMe.isChecked) {
            viewLifecycleOwner.lifecycleScope.launch {
                requireContext().dataStore.edit { preferences ->
                    preferences[Datastore.TOKEN_KEY] = data?.token ?: "Hello World"
                    preferences[Datastore.REMEMBER_KEY] = binding.cbRememberMe.isChecked
                    Log.wtf("TAG", preferences[Datastore.TOKEN_KEY])
                }
            }
        }
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment(data?.token.toString()))
    }

    private fun handleError(throwable: Throwable) {
        Snackbar.make(binding.root, throwable.message.toString(), Snackbar.LENGTH_SHORT).show()
    }
}