package com.example.ocean.ui.component.plash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import com.example.ocean.R
import com.example.ocean.Utils.Utility
import com.example.ocean.data.CountryRepositoryImpl
import com.example.ocean.data.RetrofitInstance
import com.example.ocean.databinding.FragmentStartupBinding
import com.example.ocean.domain.storage.StorageUtils
import com.example.ocean.domain.usecase.GetCountryUseCase
import com.example.ocean.ui.base.BaseFragment
import com.example.ocean.ui.component.introduction.IntroductionFragment
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

    companion object {
        private const val TIME_OUT_MILLIS = 10000L
    }

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

        // start handle downloading country flag images
        downloadListOfCountryFlag()
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

    private fun downloadListOfCountryFlag() {
        val countryRepository = CountryRepositoryImpl(RetrofitInstance.apiService)
        val scope = CoroutineScope(Dispatchers.IO)


        scope.launch {
            val deferredResults = Utility.getListOfCountryCode().asFlow().map { countryCode ->
                async {
                    Log.d(TAG, "downloadListOfCountryFlag: $countryCode")
                    val getCountryUseCase =
                        GetCountryUseCase(countryRepository, countryCode, this@StartupFragment)
                    getCountryUseCase(Unit)
                }
            }
                .buffer()
                .toList()

            try {
                // Await all deferred tasks within a specified time limit
                withTimeout(TIME_OUT_MILLIS) {
                    deferredResults.awaitAll()
                }
                Log.d(TAG, "All country flags downloaded within the time limit")
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "Timeout: Not all country flags were downloaded in time")
            }
        }
    }

    override fun storeFileInLocalStorage(byteArray: ByteArray, fileName: String) {
//        Log.d(TAG, "storeFileInLocalStorage: start storing image")
        context?.let { Utility.saveImageToDisk(it, byteArray, fileName) }
    }

}