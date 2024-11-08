package com.example.ocean.ui.component.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _: Boolean ->

        }

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

        binding.clNotification.setOnClickListener {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            } else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }
}