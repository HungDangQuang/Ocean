package com.example.ocean.ui.component.ocr

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.ocean.Utils.Constants.PROCESSED_IMAGE_HEIGHT_SIZE
import com.example.ocean.Utils.Constants.PROCESSED_IMAGE_WIDTH_SIZE
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextAnalyzer(private val onTextFound: (List<com.google.mlkit.vision.text.Text.Element>) -> Unit) :
    ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val TAG = TextAnalyzer::class.java.simpleName

    override fun analyze(imageProxy: ImageProxy) {
        @androidx.camera.core.ExperimentalGetImage
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val matrix = Matrix()
            val rotationAngle = imageProxy.imageInfo.rotationDegrees.toFloat()
            matrix.postRotate(rotationAngle)
            val originalBitmap = imageProxy.toBitmap()
            val rotatedBitmap = Bitmap.createBitmap(
                originalBitmap,
                0,
                0,
                originalBitmap.width,
                originalBitmap.height,
                matrix,
                true
            )
            val resizedBitmap = Bitmap.createScaledBitmap(rotatedBitmap, PROCESSED_IMAGE_WIDTH_SIZE, PROCESSED_IMAGE_HEIGHT_SIZE, false)

            val image = InputImage.fromBitmap(resizedBitmap, 0)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val elements =
                        visionText.textBlocks.flatMap { it.lines }.flatMap { it.elements }
                    onTextFound(elements)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Text recognition handler is failed", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
}