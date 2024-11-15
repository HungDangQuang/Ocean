package com.example.ocean.ui.component.ocr

import android.content.Intent
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@AndroidEntryPoint
class OCRFragment : BaseFragment(), CameraXConfig.Provider {

    private lateinit var binding: FragmentOcrBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProviderBinding: Camera
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var displayedTextOptionView: DisplayedTextOptionView
    private lateinit var selectionView: SelectionView
    private lateinit var noTextForTranslationView: NoTextForTranslationView
    private val viewModel: CountryListViewModel by activityViewModels()
    private var isRequestedToShowCamera = false
    private lateinit var uri: Uri
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
        if (!this::binding.isInitialized) {
            binding = FragmentOcrBinding.inflate(inflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isRequestedToShowCamera) {
            requestPermissions()
            isRequestedToShowCamera = true
        }
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

            // Remove initialized selection view
            removeSelectionView()
            binding.graphicOverlay.resetRectColor()

            // Remove initialized no text for translation view
            removeNoTextForTranslationView()
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
            goToCountryListScreen(true)
        }

        binding.outputLanguageCountry.cvInputVocabularyLanguage.setOnClickListener {
            Log.d(TAG, "Button output language clicked")
            goToCountryListScreen(false)
        }

        binding.btSelectAll.setOnClickListener {
            Log.d(TAG, "Button select all is clicked")

            // check if selection view is initialized
            removeSelectionView()

            // add and show the selection view
            selectionView = SelectionView({
                CoroutineScope(Dispatchers.Main).launch {
                    Utility.copyTextToClipboard(binding.graphicOverlay.getTextString(), requireContext())
                }
            }, requireContext()).apply {
                id = View.generateViewId()
            }

            binding.parentView.addView(selectionView)

            val constraintSet = ConstraintSet().apply {
                clone(binding.parentView)
                connect(selectionView.id, ConstraintSet.BOTTOM, binding.coordinator.id, ConstraintSet.TOP, 50)
                connect(selectionView.id, ConstraintSet.START, binding.parentView.id, ConstraintSet.START, 0)
                connect(selectionView.id, ConstraintSet.END, binding.parentView.id, ConstraintSet.END, 0)
            }
            constraintSet.setTranslationZ(selectionView.id, 3f)
            constraintSet.applyTo(binding.parentView)

            // change color of text rect
            binding.graphicOverlay.changeRectColor()

        }

        binding.btSendTextToHome.setOnClickListener {
            Log.d(TAG, "Button send text to home is clicked")
            findNavController().popBackStack()
        }

        binding.btShare.setOnClickListener {
            Log.d(TAG, "Button share is clicked")
            // test Sharesheet
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val shareIntent = Intent.createChooser(sendIntent, "Share content")
            startActivity(shareIntent)
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
                        // put ocr input text into viewModel
                        val inputText = resultText.joinToString("\n") { it.text }
                        if (inputText.isEmpty() || inputText.isBlank() || inputText == "") {
                            binding.sv.visibility = View.GONE
                            removeNoTextForTranslationView()
                            showViewNoTextForTranslation()
                        } else {
                            viewModel.setInputOCRText(inputText)
                            binding.graphicOverlay.setElements(resultText)
                        }
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
                    cameraProvider.unbindAll()

                    val bitmap = image.toBitmap()
                    val matrix = Matrix()
                    val rotationAngle = image.imageInfo.rotationDegrees.toFloat()
                    matrix.postRotate(rotationAngle)
                    val rotatedBitmap =
                        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                    image.close()
                    uri = Utility.saveImage(rotatedBitmap, requireContext())!!


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
        displayedTextOptionView = DisplayedTextOptionView( {
            removeSelectionView()
            binding.graphicOverlay.resetRectColor()
            binding.graphicOverlay.drawOriginalRect()
        }, {
            removeSelectionView()
            if (binding.graphicOverlay.isHasTranslatedText()) {
                binding.graphicOverlay.drawTranslatedRect()
            } else {
                // Translate text in the graphic view
                CoroutineScope(Dispatchers.Main).launch {
                    val textList = binding.graphicOverlay.getTextElements()
                    val translatedTextList = textList.map { text ->
                        val translatedText = suspendCoroutine { cont ->
                            viewModel.translateText(text.text) { cont.resume(it) }
                        }
                        TextCoordinates(translatedText, text.coordinates)
                    }
                    viewModel.setOutputOCRText(translatedTextList.joinToString("\n") { it.text })
                    binding.graphicOverlay.setTranslatedTextList(translatedTextList)
                    binding.graphicOverlay.drawTranslatedRect()
                }
            }

        }, requireContext()).apply {
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

    private fun removeSelectionView() {
        if (::selectionView.isInitialized) binding.parentView.removeView(selectionView)
    }

    private fun goToCountryListScreen(isSelectingInputLanguage: Boolean) {
        viewModel.setIsSelectingInputLanguage(isSelectingInputLanguage)
        findNavController().navigate(R.id.countryListFragment)
        removeSelectionView()
        binding.graphicOverlay.resetRectColor()
    }

    private fun removeNoTextForTranslationView() {
        if (::noTextForTranslationView.isInitialized) binding.parentView.removeView(noTextForTranslationView)
    }

    private fun showViewNoTextForTranslation() {
        noTextForTranslationView = NoTextForTranslationView(requireContext()).apply {
            id = View.generateViewId()
        }

        binding.clBottomSheet.addView(noTextForTranslationView)

        val constraintSet = ConstraintSet().apply {
            clone(binding.clBottomSheet)
            connect(noTextForTranslationView.id, ConstraintSet.TOP, binding.sv.id, ConstraintSet.BOTTOM, 50)
            connect(noTextForTranslationView.id, ConstraintSet.START, binding.parentView.id, ConstraintSet.START, 0)
            connect(noTextForTranslationView.id, ConstraintSet.END, binding.parentView.id, ConstraintSet.END, 0)
        }
        constraintSet.applyTo(binding.clBottomSheet)
    }

}