package com.example.ocean.ui.component.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ocean.R
import com.example.ocean.databinding.FragmentProfileBinding
import com.example.ocean.presentation.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setUpClickingEvents()
    }

    private fun observeData() {
        loginViewModel.avatarBitmap.observe(this.viewLifecycleOwner) {
            binding.ivUserAvatar.setImageBitmap(loginViewModel.avatarBitmap.value)
        }
        loginViewModel.userGmail.observe(this.viewLifecycleOwner) {
            binding.tvEmail.text = loginViewModel.userGmail.value
        }
        loginViewModel.displayName.observe(this.viewLifecycleOwner) {
            binding.tvUsername.text = loginViewModel.displayName.value
        }
    }

    private fun setUpClickingEvents() {
        binding.clSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }
    }
}