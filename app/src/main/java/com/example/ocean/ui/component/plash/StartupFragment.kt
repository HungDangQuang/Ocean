package com.example.ocean.ui.component.plash

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import com.example.ocean.R
import com.example.ocean.Utils.Utility
import com.example.ocean.databinding.FragmentStartupBinding
import com.example.ocean.domain.storage.StorageUtils
import com.example.ocean.ui.base.BaseFragment
import com.example.ocean.ui.component.introduction.IntroductionFragment
import com.example.presentation.CountryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import androidx.fragment.app.viewModels
@AndroidEntryPoint
class StartupFragment : BaseFragment(), StorageUtils {

    private lateinit var binding: FragmentStartupBinding
    private val TAG = StartupFragment::class.simpleName
    private lateinit var dialog: Dialog
    private val viewModel: CountryViewModel by viewModels()

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
    }
    
    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // todo start checking whether the images are downloaded
        viewModel.isAllFlagsDownloaded.observe(this@StartupFragment.viewLifecycleOwner) {
            if (it) {
                Log.d(TAG, "onViewCreated: all images downloaded, updated status in StartupFragment")
                goToNextScreen()
            } else {
                Log.d(TAG, "onViewCreated: flag images are not downloaded")
                showResourceDownloadingDialog()
            }
        }
        runBlocking {
            launch {
                viewModel.handleDownloadingFlagImages()
            }
        }
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
        if (this::dialog.isInitialized) {
            dialog.dismiss()
        }
    }

    override fun storeFileInLocalStorage(byteArray: ByteArray, fileName: String) {
        Log.d(TAG, "storeFileInLocalStorage: start storing image")
        Utility.saveImageToDisk(byteArray, fileName)
    }


    private fun showResourceDownloadingDialog() {
        // show dialog
        dialog = Dialog(requireContext())
        val dialogHandler = object : Utility.Companion.DialogHandler {
            override fun onPositiveClick(): View.OnClickListener {
                return View.OnClickListener {
                    dialog.dismiss()
                    // start handle downloading country flag images
                    //todo revert later
                    startLoadingAnimation()
                    runBlocking {
                        viewModel.downloadListOfCountryFlags()
                    }
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

    fun startLoadingAnimation() {
        // Hide the openNextScreen button
        binding.btnOpenIntro.visibility = View.GONE

        // start showing animation
        val screenHeight = Utility.getDeviceHeight()
        val animation = ObjectAnimator.ofFloat(binding.appContainer, "translationY", binding.ivAppLogo.y, -screenHeight.toFloat() / 3)
        animation.duration = 500
        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                binding.loadingBar.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        animation.start()
    }
}