package com.example.ocean

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.ocean.databinding.FragmentStartupBinding

class StartupFragment : BaseFragment() {

    private lateinit var binding: FragmentStartupBinding
    private val TAG = StartupFragment::class.simpleName
    
    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun setUpClickableView() {
        binding.btnOpenIntro.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
            goToNextScreen()
        }
    }

    override fun goToNextScreen() {
        Log.d(TAG, "goToNextScreen: Introduction")
        parentFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.slide_in, // enter
                R.anim.fade_out, // exit
                R.anim.fade_in, // popEnter
                R.anim.slide_out // popExit
            )
            replace(R.id.fragment_container_view, IntroductionFragment())
            addToBackStack(null)
            commit()
        }
    }

}