package com.example.ocean.ui.component.plash

import android.app.Application
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.example.ocean.OceanApplication
import com.example.ocean.R
import com.example.ocean.Utils.Utility
import com.example.ocean.data.CountryRepositoryImpl
import com.example.ocean.data.RetrofitInstance
import com.example.ocean.databinding.FragmentStartupBinding
import com.example.ocean.domain.storage.StorageUtils
import com.example.ocean.domain.usecase.GetCountryUseCase
import com.example.ocean.ui.base.BaseFragment
import com.example.ocean.ui.component.introduction.IntroductionFragment
import com.example.presentation.CountryViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class StartupFragment : BaseFragment(), StorageUtils {

    private lateinit var binding: FragmentStartupBinding
    private val TAG = StartupFragment::class.simpleName
    private lateinit var dialog: Dialog
    private lateinit var vm: com.example.presentation.CountryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // handle back pressed
        activity?.let {
            it.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Utility.showExitConfirmationDialog(it)
                }

            } )
        }

        showResourceDownloadingDialog()
    }
    
    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun setUpClickableView() {
        binding.btnOpenIntro.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
            goToNextScreen()
        }
    }

    override fun goToNextScreen() {
        Log.d(TAG, "goToNextScreen: Introduction")
        parentFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.slide_in, // enter
                R.anim.fade_out, // exit
                R.anim.fade_in, // popEnter
                R.anim.slide_out // popExit
            )
            replace(R.id.fragment_container_view, IntroductionFragment())
            addToBackStack(null)
            commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog.dismiss()
    }

    override fun storeFileInLocalStorage(byteArray: ByteArray, fileName: String) {
//        Log.d(TAG, "storeFileInLocalStorage: start storing image")
        Utility.saveImageToDisk(byteArray, fileName)
    }

    private fun setUpViewModel() {
        val countryRepository = CountryRepositoryImpl(RetrofitInstance.apiService)
        val getCountriesUseCase = GetCountryUseCase(countryRepository,"AD" ,this)
        val viewModelFactory = CountryViewModelFactory(getCountriesUseCase)
        vm = ViewModelProvider(this, viewModelFactory)[com.example.presentation.CountryViewModel::class.java]

        vm.getDownloadingStatus().observe(this
        ) {
            Log.d(TAG, "setUpViewModel: all images downloaded, updated status in StartupFragment")
            goToNextScreen()
        }

    }

    private fun showResourceDownloadingDialog() {
        // show dialog
        dialog = Dialog(requireContext())
        val dialogHandler = object : Utility.Companion.DialogHandler {
            override fun onPositiveClick(): View.OnClickListener {
                return View.OnClickListener {
                    dialog.dismiss()
                    // start handle downloading country flag images
                    setUpViewModel()
                }
            }

            override fun onNegativeClick(): View.OnClickListener {
                return View.OnClickListener {
                    dialog.dismiss()
                }
            }

        }
        Utility.showDialog(dialog, R.layout.dialog_image_downloader_confirmation, R.id.image_downloading_button_download, R.id.image_downloading_button_cancel, dialogHandler)
    }
}