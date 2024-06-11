package com.example.ocean.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpClickableView()
    }

    /*
    Override this method for view binding
     */
    protected abstract fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?

    /*
    Override this method for setting OnClick events
     */
    protected abstract fun setUpClickableView()


    /*
    Override this method for navigation to next screen
     */
    protected abstract fun goToNextScreen()

}