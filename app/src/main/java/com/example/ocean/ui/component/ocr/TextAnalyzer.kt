package com.example.ocean.ui.component.ocr

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Point
import android.graphics.PointF
import android.graphics.Rect
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.ocean.Utils.Constants.PROCESSED_IMAGE_HEIGHT_SIZE
import com.example.ocean.Utils.Constants.PROCESSED_IMAGE_WIDTH_SIZE
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text.Element
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlin.math.abs


class TextAnalyzer(private val onTextFound: (List<TextCoordinates>) -> Unit) :
    ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val TAG = TextAnalyzer::class.java.simpleName
    private val LINE_THRESHOLD = 15f
    private val MIN_CONFIDENCE = 0.85f
    private val throttleMs = 1500
    private var latestAnalyzedTimestamp = 0L;


    override fun analyze(imageProxy: ImageProxy) {

        // Only analyze image every 1500ms
        val currentTime = System.currentTimeMillis()

        if (isNeedToSkipFrame(currentTime)) {
            // Drop frame to lower FPS.
            imageProxy.close()
            return
        }

        latestAnalyzedTimestamp = currentTime
        analyzeTextInImage(imageProxy)
        imageProxy.close()
    }

    private fun isNeedToSkipFrame(currentTime:Long): Boolean {
        return currentTime - latestAnalyzedTimestamp <= throttleMs
    }

    private fun groupTextOnSameLine(elements: List<Element>): List<List<Element>> {
        val lines: MutableList<MutableList<Element>> = mutableListOf()

        for (element in elements) {
            var addedToExistingLine = false

            for (line in lines) {
                if (isOnSameLine(line[0], element)) {
                    line.add(element)
                    addedToExistingLine = true
                    break
                }
            }

            if (!addedToExistingLine) {
                val newLine = mutableListOf(element)
                lines.add(newLine)
            }
        }

        return lines
    }

    private fun isOnSameLine(e1: Element, e2: Element): Boolean {
        val e1CenterY = e1.boundingBox?.centerY() ?: 0
        val e2CenterY = e2.boundingBox?.centerY() ?: 0

        return abs(e1CenterY - e2CenterY) < LINE_THRESHOLD
    }

    @OptIn(ExperimentalGetImage::class)
    private fun analyzeTextInImage(imageProxy: ImageProxy) {
        Log.d(TAG, "start analyzing text in the image ")
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
            val resizedBitmap = Bitmap.createScaledBitmap(
                rotatedBitmap,
                PROCESSED_IMAGE_WIDTH_SIZE,
                PROCESSED_IMAGE_HEIGHT_SIZE,
                false
            )

            val image = InputImage.fromBitmap(resizedBitmap, 0)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val elements =
                        visionText.textBlocks.flatMap { it.lines }.flatMap { it.elements }

                    // separate image in lines
                    val textOnLines = groupTextOnSameLine(elements)
                    var i = 0
                    val coordinatesList = mutableListOf<TextCoordinates>()
                    for (line in textOnLines) {
                        // Process each line of text
                        i++
                        var text = ""
                        val highConfidenceTexts = line.filter { it.confidence > MIN_CONFIDENCE }
                        if (highConfidenceTexts.isNotEmpty()) {
                            val tlPoint = highConfidenceTexts[0].cornerPoints!![0]
                            val brPoint =
                                highConfidenceTexts[highConfidenceTexts.lastIndex].cornerPoints!![2]

                            for (element in highConfidenceTexts) {
                                text += element.text + " "
                            }

                            Log.d(TAG, "line $i: $text")
                            val listOfPoints = listOf(tlPoint, brPoint)
                            val textCoordinates = TextCoordinates(text, listOfPoints)
                            coordinatesList.add(textCoordinates)
                        }
                    }
                    coordinatesList.sortBy { item -> item.coordinates[0].y }
                    onTextFound(coordinatesList)
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