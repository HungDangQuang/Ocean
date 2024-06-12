package com.example.ocean.ui.component.details.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ocean.CountryViewModel
import com.example.ocean.R
import com.example.ocean.Utils.Utility
import com.example.ocean.data.Country
import com.example.ocean.data.CountryRepositoryImpl
import com.example.ocean.data.RetrofitInstance
import com.example.ocean.databinding.FragmentVocabularyAdditionBinding
import com.example.ocean.domain.storage.StorageUtils
import com.example.ocean.domain.usecase.GetCountryUseCase
import com.example.ocean.ui.base.BaseFragment
import com.example.presentation.CountryViewModelFactory

class VocabularyAdditionFragment : BaseFragment(), StorageUtils {
    private lateinit var binding:FragmentVocabularyAdditionBinding
    private lateinit var countryViewModel: CountryViewModel
    private val TAG = VocabularyAdditionFragment::class.java.simpleName
    private lateinit var vm: com.example.presentation.CountryViewModel

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVocabularyAdditionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countryViewModel = ViewModelProvider(requireActivity())[CountryViewModel::class.java]

        countryViewModel.inputLanguageCountry.observe(viewLifecycleOwner, Observer {
            // set the new country for the view
            binding.inputLanguageCountry.country = it
        })

        countryViewModel.outputLanguageCountry.observe(viewLifecycleOwner, Observer {
            binding.outputLanguageCountry.country = it
        })

        countryViewModel.inputLanguageCountry.value = Country(R.drawable.united_states_flag, "USA")
        countryViewModel.outputLanguageCountry.value = Country(R.drawable.united_kingdom_flag, "UK")
        setUpViewModel()
    }

    private fun setUpViewModel() {
        val countryRepository = CountryRepositoryImpl(RetrofitInstance.apiService)
        val getCountriesUseCase = GetCountryUseCase(countryRepository,"AD" ,this)
        val viewModelFactory = CountryViewModelFactory(getCountriesUseCase)
        vm = ViewModelProvider(this, viewModelFactory)[com.example.presentation.CountryViewModel::class.java]

    }


    override fun setUpClickableView() {

    }

    override fun goToNextScreen() {

    }

    override fun storeFileInLocalStorage(byteArray: ByteArray, fileName: String) {
        context?.let { Utility.saveImageToDisk(it, byteArray, fileName) }
    }

}