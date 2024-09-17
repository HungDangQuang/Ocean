package com.example.ocean.ui.component.ocr

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.ocean.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GraphicOverlay(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val textPaint = Paint().apply {
        color = Color.RED
        textSize = 32.0f
        textAlign = Paint.Align.CENTER
        style = Paint.Style.FILL
    }

    private val rectPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val cornerRadius = 10f

    private var elements: List<TextCoordinates> = listOf()
    val TAG = GraphicOverlay::class.java.simpleName

    init {
        // Redraw the overlay, as this graphic has been added.
        postInvalidate()

    }

    fun setElements(elements: List<TextCoordinates>) {
        this.elements = elements
        invalidate()
    }

    private fun resetElements() {
        this.elements = listOf()
        invalidate()
    }

    fun restartPaint() {
        textPaint.color = Color.RED
        textPaint.textSize = 32.0f
        resetElements()
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (elements.isNotEmpty()) {
            var tempRectF = RectF()
            for (element in elements) {

                // Measure the size of the text
                val textBounds = Rect()
                textPaint.getTextBounds(element.text, 0, element.text.length, textBounds)

                val points = element.coordinates
                var left = points[0].x.toFloat() * 1.5f
                var top = points[0].y.toFloat() * 1.8f
                var right = points[1].x.toFloat() * 1.5f + textBounds.width() * 0.8f + cornerRadius
                var bottom = points[1].y.toFloat() * 1.8f + textBounds.height() + cornerRadius

                val rectF = RectF(
                    left,
                    top,
                    right,
                    bottom
                )

                if (rectF.intersect(tempRectF) && !tempRectF.isEmpty) {

                    // Get intersection area
                    val deltaX =
                        (rectF.right - tempRectF.left).coerceAtMost(tempRectF.right - rectF.left)
                    val deltaY =
                        (rectF.bottom - tempRectF.top).coerceAtMost(tempRectF.bottom - rectF.top)

                    // Split the text box vertically
                    if (rectF.top > tempRectF.bottom) {
                        top -= deltaY
                        bottom -= deltaY
                    } else {
                        top += deltaY
                        bottom += deltaY
                    }

                    // Check for intersection after splitting
                    val temp = RectF(left, top, right, bottom)

                    if (temp.intersect(tempRectF)) {

                        // Split the text box horizontally
                        if (rectF.left < tempRectF.left) {
                            left -= deltaX
                            right -= deltaX
                        } else {
                            left += deltaX
                            right += deltaX
                        }
                    }
                }

                // Update temp rect
                tempRectF = RectF(left, top, right, bottom)

                // Draw rect
                canvas.drawRoundRect(
                    left,
                    top,
                    right,
                    bottom,
                    cornerRadius,
                    cornerRadius,
                    rectPaint
                )

                // Draw text to stay centered of rect
                val fontMetrics = textPaint.fontMetrics
                val textHeight = fontMetrics.descent - fontMetrics.ascent
                val textOffset = textHeight / 2 - fontMetrics.descent

                canvas.drawText(
                    element.text,
                    left + (right - left) / 2 + textOffset,
                    top + (bottom - top) / 2 + textOffset,
                    textPaint
                )
            }
        }
    }

    fun changeRectColor() {
        rectPaint.color = ContextCompat.getColor(context, R.color.color_maximum_blue_purple)
        invalidate()
    }

    fun resetRectColor() {
        rectPaint.color = Color.WHITE
        invalidate()
    }

    suspend fun getTextString(): String = withContext(Dispatchers.Default) {
        elements.joinToString("\n") { it.text }
    }

}