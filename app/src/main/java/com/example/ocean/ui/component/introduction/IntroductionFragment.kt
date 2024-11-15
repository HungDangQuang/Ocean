package com.example.ocean.ui.component.introduction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.ocean.ui.component.home.MainActivity
import com.example.ocean.R
import com.example.ocean.databinding.FragmentIntroductionBinding
import com.example.ocean.ui.base.BaseFragment

class IntroductionFragment : BaseFragment() {

    private val TAG = IntroductionFragment::class.simpleName
    private lateinit var binding:FragmentIntroductionBinding

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroductionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun setUpClickableView() {
        binding.btnOpenHome.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
            goToNextScreen()
        }
    }

    override fun goToNextScreen() {
        Log.d(TAG, "goToNextScreen: Home")
        parentFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.slide_in, // enter
                R.anim.fade_out, // exit
                R.anim.fade_in, // popEnter
                R.anim.slide_out // popExit
            )
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

}