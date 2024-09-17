package com.example.ocean.ui.component.ocr

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.ocean.R
import com.example.ocean.databinding.LayoutOptionTextBinding

@SuppressLint("ViewConstructor")
class DisplayedTextOptionView @JvmOverloads constructor(
    originalModeCallback: (() -> Unit)?,
    translatedModeCallback: (() -> Unit)?,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val TAG = DisplayedTextOptionView::class.java.simpleName
    private var binding:LayoutOptionTextBinding =
        LayoutOptionTextBinding.inflate(LayoutInflater.from(context), this, true)
    private var isShowingOriginalText = true

    init {
        setUpChildViewOnClickListener(originalModeCallback, translatedModeCallback)
    }

    private fun animateChangingButtonColor(color1: Int, color2: Int, view: View) {
        val animator = ValueAnimator.ofObject(ArgbEvaluator(), color1, color2)
        animator.duration = 500
        animator.addUpdateListener {
            val interpolatedColor = it.animatedValue as Int
            val updatedColorStateList = ColorStateList.valueOf(interpolatedColor)
            view.backgroundTintList = updatedColorStateList
        }
        animator.start()
    }

    private fun setUpChildViewOnClickListener(
        originalModeCallback: (() -> Unit)? = null,
        translatedModeCallback: (() -> Unit)? = null
    ) {
        // Original and target colors
        val color1 = ContextCompat.getColor(context, R.color.color_pale_spring_bud)
        val color2 = ContextCompat.getColor(context, R.color.color_floral_white)

        binding.btTranslatedText.setOnClickListener {
            Log.d(TAG, "button show translated text clicked")
            if (isShowingOriginalText) {
                animateChangingButtonColor(color2, color1, it)
                animateChangingButtonColor(color1, color2, binding.btOriginalText)
                isShowingOriginalText = false
                translatedModeCallback?.invoke()
            }
        }

        binding.btOriginalText.setOnClickListener {
            Log.d(TAG, "button show original text clicked")
            if (!isShowingOriginalText) {
                animateChangingButtonColor(color2, color1, it)
                animateChangingButtonColor(color1, color2, binding.btTranslatedText)
                isShowingOriginalText = true
                originalModeCallback?.invoke()
            }
        }
    }

    override fun getAccessibilityPaneTitle(): CharSequence? {
        return super.getAccessibilityPaneTitle()
    }


}