package com.bchmsl.task6.ui.home

import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bchmsl.task6.databinding.FragmentHomeBinding
import com.bchmsl.task6.datastore.Datastore.dataStore
import com.bchmsl.task6.ui.base.BaseFragment
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val args: HomeFragmentArgs by navArgs()
    override fun start() {
        setText()
        listeners()
    }

    private fun listeners() {
        binding.btnLogout.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch{
                requireContext().dataStore.edit {
                    it.clear()
                }
            }
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
        }
    }

    private fun setText() {
        val token = args.token
        binding.tvToken.text = "Your token is $token"
    }
}