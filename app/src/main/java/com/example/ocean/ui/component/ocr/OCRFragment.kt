package com.example.ocean.ui.component.ocr

import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraXConfig
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ocean.OceanApplication
import com.example.ocean.R
import com.example.ocean.Utils.Utility
import com.example.ocean.databinding.FragmentOcrBinding
import com.example.ocean.presentation.CountryListViewModel
import com.example.ocean.ui.base.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@AndroidEntryPoint
class OCRFragment : BaseFragment(), CameraXConfig.Provider {

    private lateinit var binding: FragmentOcrBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProviderBinding: Camera
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var displayedTextOptionView: DisplayedTextOptionView
    private val viewModel: CountryListViewModel by activityViewModels()
    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                android.Manifest.permission.CAMERA,
            ).apply {
            }.toTypedArray()
    }

    private var pickMedia: ActivityResultLauncher<PickVisualMediaRequest> = registerForActivityResult(
        PickVisualMedia()
    ) { uri: Uri? ->
        // Callback is invoked after the user selects a media item or closes the photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(
                    requireContext(),
                    "Permission request denied",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startCamera()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpViewModel()
        binding = FragmentOcrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
    }


    override fun setUpClickableView() {

        binding.backButton.setOnClickListener {
            Log.d(TAG, "Back to vocabulary screen")
            findNavController().popBackStack(R.id.vocabularyAdditionFragment, false)
        }

        binding.flashButton.setOnClickListener {
            Log.d(TAG, "Clicked flash on/off button")
            val isTorchOn = cameraProviderBinding.cameraInfo.torchState.value == TorchState.ON
            cameraProviderBinding.cameraControl.enableTorch(!isTorchOn)
            binding.flashButton.setImageResource(
                if (isTorchOn) R.drawable.flash_off_icon else R.drawable.flash_on_icon
            )
        }

        binding.btImageCapture.setOnClickListener {
            takePhoto()
        }

        binding.btExitPreviewMode.setOnClickListener {
            Log.d(TAG, "Exit preview mode")
            binding.clBottomSheet.visibility = View.GONE
            binding.backButton.visibility = View.VISIBLE
            it.visibility = View.GONE
            binding.ivCapturedImage.visibility = View.GONE
            binding.graphicOverlay.restartPaint()
            startCamera()

            // Remove initialized text option view
            binding.parentView.removeView(displayedTextOptionView)
        }

        binding.btGallery.setOnClickListener {
            Log.d(TAG, "Button Gallery clicked")
            pickMedia.launch(
                PickVisualMediaRequest.Builder()
                    .setMediaType(ImageOnly)
                    .build()
            )
        }

        binding.ivRevert.setOnClickListener {
            Log.d(TAG, "Button revert language clicked")
            viewModel.revertInOutLanguage()
        }

        binding.inputLanguageCountry.cvInputVocabularyLanguage.setOnClickListener {
            Log.d(TAG, "Button input language clicked")
            viewModel.setIsSelectingInputLanguage(true)
            findNavController().navigate(R.id.countryListFragment)
        }

        binding.outputLanguageCountry.cvInputVocabularyLanguage.setOnClickListener {
            Log.d(TAG, "Button output language clicked")
            viewModel.setIsSelectingInputLanguage(false)
            findNavController().navigate(R.id.countryListFragment)
        }

    }

    override fun goToNextScreen() {

    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .setResolutionSelector(
                    ResolutionSelector.Builder()
                        .setResolutionStrategy(
                            ResolutionStrategy(
                                Size(
                                    Utility.getDeviceWidth(),
                                    Utility.getDeviceWidth() * 16 / 9
                                ),  // Preferred resolution
                                ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER
                            )
                        )
                        .setAspectRatioStrategy(
                            AspectRatioStrategy(
                                AspectRatio.RATIO_16_9,
                                AspectRatioStrategy.FALLBACK_RULE_AUTO
                            )
                        )
                        .build()
                )
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setResolutionSelector(
                    ResolutionSelector.Builder()
                        .setResolutionStrategy(
                            ResolutionStrategy(
                                Size(
                                    Utility.getDeviceWidth(),
                                    Utility.getDeviceWidth() * 16 / 9
                                ),  // Preferred resolution
                                ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER
                            )
                        )
                        .setAspectRatioStrategy(
                            AspectRatioStrategy(
                                AspectRatio.RATIO_16_9,
                                AspectRatioStrategy.FALLBACK_RULE_AUTO
                            )
                        )
                        .build()
                )
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, TextAnalyzer { resultText ->
                        binding.graphicOverlay.setElements(resultText)
                    })
                }

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Create image capture
                imageCapture = ImageCapture.Builder().build()
                // Bind use cases to camera
                cameraProviderBinding = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(OceanApplication.applicationContext()))
    }

    private fun takePhoto() {
        Log.d(TAG, "takePhoto")
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    Log.d(TAG, "onCaptureSuccess")

                    // shutdown camera before showing image
                    shutDownCamera()

                    val bitmap = image.toBitmap()
                    val matrix = Matrix()
                    val rotationAngle = image.imageInfo.rotationDegrees.toFloat()
                    matrix.postRotate(rotationAngle)
                    val rotatedBitmap =
                        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                    image.close()


                    // show the captured image

                    binding.ivCapturedImage.setImageBitmap(rotatedBitmap)
                    handleUIAfterTakingPhoto()
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e(TAG, "onError: Exception when capturing image", exception)
                }
            })
    }

    private fun shutDownCamera() {
        cameraProvider.unbindAll()
        binding.graphicOverlay.resetElements()
//        cameraExecutor.shutdown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        shutDownCamera()
    }

    private fun handleUIAfterTakingPhoto() {

        // change the back button to 'x' button
        binding.backButton.visibility = View.INVISIBLE
        binding.btExitPreviewMode.visibility = View.VISIBLE

        // show the opacity view
        binding.vOpacity.visibility = View.VISIBLE

        // show the original - translated image UI
        if (::displayedTextOptionView.isInitialized) {
            binding.parentView.removeView(displayedTextOptionView)
        }
        displayedTextOptionView = DisplayedTextOptionView(requireContext()).apply {
            id = View.generateViewId()
        }
        binding.parentView.addView(displayedTextOptionView)
        val constraintSet = ConstraintSet().apply {
            clone(binding.parentView)
            connect(displayedTextOptionView.id, ConstraintSet.TOP, binding.guideline20Percent.id, ConstraintSet.BOTTOM, 0) // Margin of 50dp from the top
            connect(displayedTextOptionView.id, ConstraintSet.START, binding.parentView.id, ConstraintSet.START, 0)
            connect(displayedTextOptionView.id, ConstraintSet.END, binding.parentView.id, ConstraintSet.END, 0)
        }
        constraintSet.setTranslationZ(displayedTextOptionView.id, 3f)
        constraintSet.applyTo(binding.parentView)

        // show the bottom sheet layout

        binding.clBottomSheet.visibility = View.VISIBLE
        BottomSheetBehavior.from(binding.clBottomSheet).apply {
            this.state = BottomSheetBehavior.STATE_EXPANDED
            isDraggable = false
        }

    }

    override fun getCameraXConfig(): CameraXConfig {
        return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
            .setAvailableCamerasLimiter(CameraSelector.DEFAULT_BACK_CAMERA)
            .build()
    }

    private fun setUpViewModel() {
        viewModel.currentInputCountry.observe(viewLifecycleOwner) {
            binding.inputLanguageCountry.tvInputLanguageCountry.text = it
        }

        viewModel.currentOutputCountry.observe(viewLifecycleOwner) {
            binding.outputLanguageCountry.tvInputLanguageCountry.text = it
        }
    }

}