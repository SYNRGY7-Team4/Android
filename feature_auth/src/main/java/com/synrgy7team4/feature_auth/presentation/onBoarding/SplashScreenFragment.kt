package com.synrgy7team4.feature_auth.presentation.onBoarding

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.synrgy7team4.feature_auth.R
import com.synrgy7team4.feature_auth.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AnimatorSet().apply {
            playSequentially(playMoveScaling(), playFadeAnimation())
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (isAdded && view != null) { // Check if the fragment is still added and view is not null
                        view.findNavController().navigate(R.id.action_splashScreenFragment_to_onBoardingFragment)
                    }
                }
            })
            start()
        }
    }

    private fun playMoveScaling(): AnimatorSet {
        val imageSplash = binding.splashSlide

        val moveAnimate = ObjectAnimator.ofFloat(imageSplash, View.TRANSLATION_Y, 2800f, -100f).setDuration(7000)
        val scalingAnimate = ObjectAnimator.ofFloat(imageSplash, View.SCALE_Y, 0f, 4f).setDuration(7000)

        return AnimatorSet().apply {
            playTogether(moveAnimate, scalingAnimate)
        }
    }

    private fun playFadeAnimation(): AnimatorSet {
        val imageSplash = binding.splashSlide
        val fadeAnimate = ObjectAnimator.ofFloat(imageSplash, View.ALPHA, 1f, 0f).setDuration(3000)
        val logoFade = ObjectAnimator.ofFloat(binding.lumi, View.ALPHA, 1f, 0f).setDuration(1000)
        return AnimatorSet().apply {
            playTogether(fadeAnimate, logoFade)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

