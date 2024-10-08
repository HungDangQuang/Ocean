package com.example.ocean.ui.component.details.add

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ocean.R
import com.example.ocean.Utils.Utility.Companion.isPermissionGranted
import com.example.ocean.databinding.FragmentVocabularyAdditionBinding
import com.example.ocean.presentation.CountryListViewModel
import com.example.ocean.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class VocabularyAdditionFragment : BaseFragment() {
    private lateinit var binding: FragmentVocabularyAdditionBinding
    private val countryListViewModel: CountryListViewModel by activityViewModels()
    private val TAG = VocabularyAdditionFragment::class.java.simpleName

    private val launcher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val resultString =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)

            binding.tiInputText.setText(resultString)
        }
    }

    private val permissionResultCallback = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Log.d(TAG, "Permission granted")
            startRecording()
        } else {
            Log.d(TAG, "Permission is denied")
        }
    }

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
        handleOCRInputText()
        handleOCROutputText()
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
                Toast.makeText(
                    requireContext(),
                    R.string.toast_message_no_input,
                    Toast.LENGTH_SHORT
                )
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

        binding.ivVoiceRecording.setOnClickListener {
            Log.d(TAG, "Clicked voice button")
            if (!isPermissionGranted(requireContext(), Manifest.permission.RECORD_AUDIO)
            ) {
                permissionResultCallback.launch(Manifest.permission.RECORD_AUDIO)
            } else {
                startRecording()
            }
        }

        binding.ivOcr.setOnClickListener {
            findNavController().navigate(R.id.ocrFragment)
        }
    }

    private fun setUpTextChangedBehavior() {
        binding.tiInputText.addTextChangedListener(object :
            TextChangedListener(binding.btDeleteText) {})
    }

    override fun goToNextScreen() {
        findNavController().navigate(R.id.countryListFragment)
    }

    private fun setUpCountryListViewModel() {
        Log.d(TAG, "setUpCountryListViewModel")

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

    private fun startRecording() {
        Log.d(TAG, "startRecording")
        val speechRecognizerIntent: Intent =
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            }

        launcher.launch(speechRecognizerIntent)
    }

    private fun handleOCRInputText() {
        countryListViewModel.inputOCRText.value?.takeIf { it.isNotEmpty() }?.let {
            binding.tiInputText.post {
                binding.tiInputText.setText(it)
            }
        }
    }

    private fun handleOCROutputText() {
        countryListViewModel.outputOCRText.value?.takeIf { it.isNotEmpty() }?.let {
            binding.clOutputText.visibility = View.VISIBLE
            binding.tvOutputText.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // reset ocr result
        countryListViewModel.setOutputOCRText("")
        countryListViewModel.setInputOCRText("")
    }

}