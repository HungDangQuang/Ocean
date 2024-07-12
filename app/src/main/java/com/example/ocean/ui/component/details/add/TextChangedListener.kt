package com.example.ocean.ui.component.details.add

import android.text.Editable
import android.text.TextWatcher
import android.view.View

abstract class TextChangedListener(val view: View) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        view.visibility = if ((s?.length ?: 0) > 0) View.VISIBLE else View.GONE
    }

    override fun afterTextChanged(s: Editable?) {
    }

}