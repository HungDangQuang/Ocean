package com.example.ocean.ui.component.ocr

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ocean.databinding.LayoutNoTextForTranslationBinding

class NoTextForTranslationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutNoTextForTranslationBinding.inflate(LayoutInflater.from(context), this, true)
    }

}