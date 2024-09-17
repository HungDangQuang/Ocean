package com.example.ocean.ui.component.ocr

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ocean.databinding.ViewSelectionBinding

@SuppressLint("ViewConstructor")
class SelectionView @JvmOverloads constructor(
    copyBtnCallback: (() -> Unit)?,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val TAG = SelectionView::class.java.simpleName
    private var binding: ViewSelectionBinding =
        ViewSelectionBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setUpClickListeners(copyBtnCallback)
    }

    private fun setUpClickListeners(copyBtnCallback: (() -> Unit)?) {

        binding.btCopy.setOnClickListener {
            Log.d(TAG, "Button Copy is clicked")
            copyBtnCallback?.invoke()
        }

        binding.btTranslate.setOnClickListener {
            Log.d(TAG, "Button translate is clicked")
        }

    }

}