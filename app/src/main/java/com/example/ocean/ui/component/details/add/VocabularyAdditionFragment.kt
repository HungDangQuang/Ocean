package com.example.ocean.ui.component.details.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ocean.R
import com.example.ocean.Utils.Constants
import com.example.ocean.data.repository.DataStoreRepositoryImpl
import com.example.ocean.data.repository.LocalStorageRepositoryImpl
import com.example.ocean.data.repository.TranslationRepositoryImpl
import com.example.ocean.data.repository.datasource.DataStorePreferences
import com.example.ocean.data.service.RetrofitInstance
import com.example.ocean.databinding.FragmentVocabularyAdditionBinding
import com.example.ocean.domain.storage.DataStoreKey
import com.example.ocean.domain.usecase.GetCurrentCountryUseCase
import com.example.ocean.domain.usecase.StoreCurrentCountryUseCase
import com.example.ocean.domain.usecase.TranslateTextUseCase
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTextChangedBehavior()
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

        binding.btTranslate.setOnClickListener {
            binding.tiInputText.onEditorAction(EditorInfo.IME_ACTION_DONE)
            if (isTextInputEmpty()) {
                Toast.makeText(requireContext(), R.string.toast_message_no_input, Toast.LENGTH_SHORT)
                    .show()
            } else {
                // translate text
                countryListViewModel.translateText(binding.tiInputText.text.toString())

                // perform animation
                if (binding.clOutputText.visibility == View.GONE) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.progressBar.alpha = 0f
                    Log.d(TAG, "setUpClickableView: ${binding.progressBar.height.toFloat()}")
                    binding.progressBar.translationY = -binding.progressBar.height.toFloat()
                    binding.progressBar.animate()
                        .translationY(0f)
                        .alpha(1f)
                        .setDuration(500)
                        .start()
                } else {
                    it.animate()
                        .alpha(0f)
                        .setDuration(200)
                        .withEndAction {
                            it.visibility = View.INVISIBLE
                            binding.smallProgressBar.visibility = View.VISIBLE
                        }
                }
            }
        }

        binding.btDeleteText.setOnClickListener {
            binding.tiInputText.setText("")
        }

        binding.btDeleteOutputText.setOnClickListener {
            binding.clOutputText.visibility = View.GONE
            binding.tvOutputText.text = ""
        }

        binding.ivRevert.setOnClickListener {
            countryListViewModel.revertInOutLanguage()
        }
    }

    private fun setUpTextChangedBehavior() {
        binding.tiInputText.addTextChangedListener(object : TextChangedListener(binding.btDeleteText){})
    }

    override fun goToNextScreen() {
        findNavController().navigate(R.id.countryListFragment)
    }

    private fun setUpCountryListViewModel() {
        Log.d(TAG, "setUpCountryListViewModel")
        val dataStorePreferences = DataStorePreferences(requireContext())
        val dataStoreRepositoryImpl = DataStoreRepositoryImpl(dataStorePreferences)
        val getCurrentInputCountryUseCase = GetCurrentCountryUseCase(
            DataStoreKey.KEY_CURRENT_INPUT_COUNTRY_NAME,
            dataStoreRepositoryImpl,
            Constants.DEFAULT_INPUT_COUNTRY
        )
        val getCurrentOutputCountryUseCase = GetCurrentCountryUseCase(
            DataStoreKey.KEY_CURRENT_OUTPUT_COUNTRY_NAME,
            dataStoreRepositoryImpl,
            Constants.DEFAULT_OUTPUT_COUNTRY
        )
        val storeCurrentInputCountryUseCase = StoreCurrentCountryUseCase(DataStoreKey.KEY_CURRENT_INPUT_COUNTRY_NAME, dataStoreRepositoryImpl)
        val storeCurrentOutputCountryUseCase = StoreCurrentCountryUseCase(DataStoreKey.KEY_CURRENT_OUTPUT_COUNTRY_NAME, dataStoreRepositoryImpl)
        val localStorageRepositoryImpl = LocalStorageRepositoryImpl()

        val translationRepositoryImpl = TranslationRepositoryImpl(RetrofitInstance.translationService)
        val translateTextUseCase = TranslateTextUseCase(translationRepositoryImpl)

        val viewModelFactory = CountryListViewModelFactory(
            localStorageRepositoryImpl,
            getCurrentInputCountryUseCase,
            storeCurrentInputCountryUseCase,
            getCurrentOutputCountryUseCase,
            storeCurrentOutputCountryUseCase,
            translateTextUseCase
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

        countryListViewModel.translatedText.observe(viewLifecycleOwner) {
            Log.d(TAG, "setUpCountryListViewModel: translated text $it")
            // set the output text
            binding.tvOutputText.text = it

            // perform animation
            if (binding.clOutputText.visibility == View.GONE) {
                binding.clOutputText.visibility = View.VISIBLE
                binding.clOutputText.alpha = 0f
                binding.progressBar.visibility = View.GONE
                binding.clOutputText.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .start()
            } else {
                binding.btTranslate.visibility = View.VISIBLE
                binding.btTranslate.animate()
                    .alpha(1f)
                    .setDuration(200)
                    .withEndAction {
                        binding.smallProgressBar.visibility = View.GONE
                    }
            }
        }

    }

    private fun isTextInputEmpty(): Boolean {
        return binding.tiInputText.text?.length == 0
    }

}