package com.example.ocean.ui.component.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ocean.R
import com.example.ocean.Utils.Utility
import com.example.ocean.databinding.FragmentLoginBinding
import com.example.ocean.presentation.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding:FragmentLoginBinding
    private val loginViewModel:LoginViewModel by activityViewModels()
    private val TAG = LoginFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: HUNG")
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tiEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.layoutEmail.error = null
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btSignIn.setOnClickListener {

            // check email error
            checkEmailInputError()

            // check password error
            checkPasswordError()
        }

        binding.ivGoogle.setOnClickListener {
            Log.d(TAG, "SignInWithGoogle Button is clicked")
            loginViewModel.loginWithGoogleEmail()
        }

        observeData()

    }

    private fun checkPasswordError() {
        Log.d(TAG, "checkPasswordError")
        if (binding.tiPassword.text.toString().isEmpty()) {
            binding.layoutPassword.error = getString(R.string.error_message_no_password_input)
        } else {
            binding.layoutPassword.error = null
        }
    }

    private fun checkEmailInputError() {
        Log.d(TAG, "checkEmailError")
        val email = binding.tiEmail.text.toString()

        binding.layoutEmail.error = when {
            email.isEmpty() -> getString(R.string.error_message_no_email_input)
            !Utility.isValidEmail(email) -> getString(R.string.error_message_email_not_valid)
            else -> null
        }
    }

    private fun observeData() {
        loginViewModel.avatarBitmapAbsoluteFilePath.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.profileFragment)
        }
    }
}