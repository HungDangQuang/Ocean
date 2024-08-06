package com.example.ocean.ui.component.ocr

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.ocean.Utils.Constants.PROCESSED_IMAGE_HEIGHT_SIZE
import com.example.ocean.Utils.Constants.PROCESSED_IMAGE_WIDTH_SIZE
import com.google.mlkit.vision.text.Text

class GraphicOverlay(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val textPaint = Paint().apply {
        color = Color.RED
        textSize = 32.0f
    }

    private var elements: List<Text.Element> = listOf()
    val TAG = GraphicOverlay::class.java.simpleName

    init {
        // Redraw the overlay, as this graphic has been added.
        postInvalidate()

    }

    fun setElements(elements: List<Text.Element>) {
        this.elements = elements
        invalidate()
    }

    fun resetElements() {
        this.elements = listOf()
//        textPaint.reset()
        invalidate()
    }

    fun restartPaint() {
        textPaint.color = Color.RED
        textPaint.textSize = 32.0f
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val widthRatio: Float = (PROCESSED_IMAGE_WIDTH_SIZE.toFloat() / width )
        val heightRatio: Float = (PROCESSED_IMAGE_HEIGHT_SIZE / ((1.8f *width) + 0.5f * (1.8f*width - PROCESSED_IMAGE_HEIGHT_SIZE)))

        for (element in elements) {
            val rect = element.boundingBox
            if (element.confidence > 0.7f && rect != null) {
                val point = element.cornerPoints!![0]
                canvas.drawText(
                    element.text,
                    (point.x.toFloat() / widthRatio),
                    (point.y.toFloat() / heightRatio),
                    textPaint
                )
            }
        }
    }

}