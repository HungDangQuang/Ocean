package com.example.ocean.ui.component.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ocean.R
import com.example.ocean.Utils.Utility.Companion.isValidEmail
import com.example.ocean.databinding.ActivityLoginBinding
import com.example.ocean.presentation.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private val TAG = LoginActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            !isValidEmail(email) -> getString(R.string.error_message_email_not_valid)
            else -> null
        }
    }
}