package com.example.ocean.ui.component.details.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ocean.R
import com.example.ocean.data.repository.LocalStorageRepositoryImpl
import com.example.ocean.databinding.FragmentVocabularyAdditionBinding
import com.example.ocean.presentation.CountryListViewModel
import com.example.ocean.presentation.CountryListViewModelFactory
import com.example.ocean.ui.base.BaseFragment

class VocabularyAdditionFragment : BaseFragment() {
    private lateinit var binding:FragmentVocabularyAdditionBinding
    private lateinit var countryListViewModel: CountryListViewModel
    private val TAG = VocabularyAdditionFragment::class.java.simpleName

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setUpCountryListViewModel()
        binding = FragmentVocabularyAdditionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setUpClickableView() {
        binding.inputLanguageCountry.cvInputVocabularyLanguage.setOnClickListener {
            countryListViewModel.setIsSelectingInputLanguage(true)
            goToNextScreen()
        }

        binding.outputLanguageCountry.cvInputVocabularyLanguage.setOnClickListener {
            countryListViewModel.setIsSelectingInputLanguage(false)
            goToNextScreen()
        }
    }

    override fun goToNextScreen() {
        findNavController().navigate(R.id.countryListFragment)
    }

    private fun setUpCountryListViewModel() {
        Log.d(TAG, "setUpCountryListViewModel")
        val localStorageRepositoryImpl = LocalStorageRepositoryImpl()
        val viewModelFactory = CountryListViewModelFactory(
            localStorageRepositoryImpl
        )
        countryListViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[CountryListViewModel::class.java]

        countryListViewModel.currentInputCountry.observe(viewLifecycleOwner) {
            Log.d(TAG, "update the value of the current input country")
            binding.inputLanguageCountry.tvInputLanguageCountry.text = it
        }

        countryListViewModel.currentOutputCountry.observe(viewLifecycleOwner) {
            Log.d(TAG, "update the value of the current output country")
            binding.outputLanguageCountry.tvInputLanguageCountry.text = it
        }

    }

}