package com.example.ocean.ui.component.ocr

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ocean.databinding.ViewSelectionBinding

class SelectionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val TAG = SelectionView::class.java.simpleName
    private var binding: ViewSelectionBinding =
        ViewSelectionBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setUpClickListeners()
    }

    private fun setUpClickListeners() {

        binding.btCopy.setOnClickListener {
            Log.d(TAG, "Button Copy is clicked")
        }

        binding.btTranslate.setOnClickListener {
            Log.d(TAG, "Button translate is clicked")
        }

    }

}