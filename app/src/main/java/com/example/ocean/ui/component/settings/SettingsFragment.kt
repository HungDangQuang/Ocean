package com.example.ocean.ui.component.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.example.ocean.Utils.Constants
import com.example.ocean.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private lateinit var binding:FragmentSettingsBinding
    private var isSetUpDarkTheme = false
    private val TAG = SettingsFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isSetUpDarkTheme = savedInstanceState?.getBoolean(Constants.KEY_DARK_THEME_STATUS)
            ?: when(AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.MODE_NIGHT_YES -> true
                else -> false
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.switchDarkTheme.isChecked = isSetUpDarkTheme
        setUpClickingEvent()
    }

    private fun setUpClickingEvent() {
        binding.clTheme.setOnClickListener {
            Log.d(TAG, "setUpClickingEvent: clTheme is clicked")
            binding.switchDarkTheme.performClick()
        }

        binding.switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Dark theme is enabled
                Log.d(TAG, "dark theme is going to set")
            } else {
                // Dark theme is disabled
                Log.d(TAG, "light theme is going to set")
            }
            setAppTheme()
        }

    }

    private fun setAppTheme() {
        if (isSetUpDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            isSetUpDarkTheme = false
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            isSetUpDarkTheme = true
        }
        requireActivity().recreate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(Constants.KEY_DARK_THEME_STATUS, isSetUpDarkTheme)
    }
}