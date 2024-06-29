package com.example.ocean.ui.component.details.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ocean.CountryViewModel
import com.example.ocean.R
import com.example.ocean.data.Country
import com.example.ocean.databinding.FragmentVocabularyAdditionBinding
import com.example.ocean.ui.base.BaseFragment

class VocabularyAdditionFragment : BaseFragment() {
    private lateinit var binding:FragmentVocabularyAdditionBinding
    private lateinit var countryViewModel: CountryViewModel
    private val TAG = VocabularyAdditionFragment::class.java.simpleName

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

        binding.inputLanguageCountry.cvInputVocabularyLanguage.setOnClickListener {
            goToNextScreen()
        }
    }

    override fun setUpClickableView() {

    }

    override fun goToNextScreen() {
        findNavController().navigate(R.id.countryListFragment)
    }

}