package com.example.ocean.ui.component.ocr

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View

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

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        val widthRatio: Float = (PROCESSED_IMAGE_WIDTH_SIZE.toFloat() / width )
//        val heightRatio: Float = (PROCESSED_IMAGE_HEIGHT_SIZE / ((1.8f *width) + 0.5f * (1.8f*width - PROCESSED_IMAGE_HEIGHT_SIZE)))

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

                Log.d(TAG, "text: ${element.text} height: ${bottom - top}")


                val rectF = RectF(
                    left,
                    top,
                    right,
                    bottom
                )

                if (rectF.intersect(tempRectF) && !tempRectF.isEmpty) {
                    val deltaX = Math.min(rectF.right - tempRectF.left, tempRectF.right - rectF.left)
                    val deltaY = Math.min(rectF.bottom - tempRectF.top, tempRectF.bottom - rectF.top)

                    if (deltaX > deltaY) {
                        if (rectF.left < tempRectF.left) {
                            left -= deltaX
                            right -= deltaX
//                            rectF.offset(-deltaX, 0f) // Move rect1 to the left
                        } else {
                            left += deltaX
                            right += deltaX
//                            rectF.offset(deltaX, 0f) // Move rect1 to the right
                        }
                    } else {
                        if (rectF.top < tempRectF.top) {
                            top -= deltaY
                            bottom -= deltaY
//                            rectF.offset(0f, -deltaY) // Move rect1 upwards
                        } else {
                            top += deltaY
                            bottom += deltaY
//                            rectF.offset(0f, deltaY) // Move rect1 downwards
                        }
                    }

//                    if (tempRectF.bottom - rectF.top > 0 && tempRectF.right - rectF.left > 0) {
//                        top = tempRectF.bottom
//                        bottom = rectF.bottom + 40f + (tempRectF.bottom - rectF.top)
//                    } else {
//                        top = rectF.top - (rectF.bottom - tempRectF.top)
//                        bottom = rectF.bottom - (rectF.bottom - tempRectF.top)
//                    }
//                    val horizontalSplitRect = RectF(left, top, right, bottom)
//                    if (horizontalSplitRect.intersect(tempRectF)) {
//                        if (tempRectF.right > horizontalSplitRect.left) {
//                            left = horizontalSplitRect.left + (horizontalSplitRect.right - tempRectF.left)
//                            right = horizontalSplitRect.right + (horizontalSplitRect.right - tempRectF.left)
//                        }
//                    }

                }

                tempRectF = rectF

                canvas.drawRoundRect(left, top, right, bottom, cornerRadius, cornerRadius, rectPaint)

                val fontMetrics = textPaint.fontMetrics
                val textHeight = fontMetrics.descent - fontMetrics.ascent
                val textOffset = textHeight / 2 - fontMetrics.descent
                Log.d(TAG, "text: ${element.text} height: ${bottom - top} width: ${right - left} height from rect: ${rectF.bottom - rectF.top}")

                canvas.drawText(
                    element.text,
                    left + (right - left)/2 + textOffset,
                    top + (bottom - top)/2 + textOffset,
                    textPaint
                )
            }
        }
    }

    private fun translateRectVertical(rootRect: RectF, comparativeRect: RectF): RectF {

        if (rootRect.top < comparativeRect.bottom) {
            val adjustedTop = rootRect.bottom + 20f
            val adjustedBottom = comparativeRect.bottom + 20f + (rootRect.bottom - comparativeRect.top)
            return RectF(comparativeRect.left, adjustedTop, comparativeRect.right, adjustedBottom)
        }
        val adjustedTop = comparativeRect.top - (comparativeRect.bottom - rootRect.top)
        val adjustedBottom = comparativeRect.bottom - (comparativeRect.bottom - rootRect.top)
        return  RectF(comparativeRect.left, adjustedTop, comparativeRect.right, adjustedBottom)
    }

    private fun translateRectHorizontal(rootRect: RectF, comparativeRect: RectF): RectF {
        if (rootRect.right > comparativeRect.left) {
            val adjustedLeft = comparativeRect.left + (comparativeRect.right - rootRect.left)
            val adjustedRight = comparativeRect.right + (comparativeRect.right - rootRect.left)
            return RectF(adjustedLeft, comparativeRect.top, adjustedRight, comparativeRect.bottom)
        }
        return comparativeRect
    }

}