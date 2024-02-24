package com.example.ocean

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ocean.databinding.FragmentIntroductionBinding
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
        TODO("Not yet implemented")
    }

    override fun goToNextScreen() {
        TODO("Not yet implemented")
    }

}