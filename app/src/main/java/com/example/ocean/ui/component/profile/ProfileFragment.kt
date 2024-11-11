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
import com.example.ocean.Utils.Constants.KEY_IMAGE_PATH
import com.example.ocean.Utils.Constants.KEY_USER_EMAIL
import com.example.ocean.Utils.Constants.KEY_USER_NAME
import com.example.ocean.Utils.Utility
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

        // Check whether user info needs to be reloaded
        if (savedInstanceState != null) {
            binding.ivUserAvatar.setImageBitmap(Utility.loadImageBitmapFromAbsolutePath(savedInstanceState.getString(KEY_IMAGE_PATH)))
            binding.tvEmail.text = savedInstanceState.getString(KEY_USER_EMAIL)
            binding.tvUsername.text = savedInstanceState.getString(KEY_USER_NAME)
            savedInstanceState.clear()
        }
    }

    private fun observeData() {
        loginViewModel.avatarBitmapAbsoluteFilePath.observe(this.viewLifecycleOwner) {
            binding.ivUserAvatar.setImageBitmap(Utility.loadImageBitmapFromAbsolutePath(loginViewModel.avatarBitmapAbsoluteFilePath.value))
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_USER_NAME, loginViewModel.displayName.value)
        outState.putString(KEY_USER_EMAIL, loginViewModel.userGmail.value)
        outState.putString(KEY_IMAGE_PATH, loginViewModel.avatarBitmapAbsoluteFilePath.value)
    }
}