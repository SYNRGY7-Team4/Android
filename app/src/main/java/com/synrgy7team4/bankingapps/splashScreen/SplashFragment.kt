package com.synrgy7team4.bankingapps.splashScreen

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.synrgy7team4.bankingapps.R
import com.synrgy7team4.bankingapps.databinding.FragmentSplashBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SplashViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSplashBinding.inflate(layoutInflater).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnimatorSet().apply {
            playSequentially(playMoveScaling(), playFadeAnimation())
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    viewModel.checkJwtTokenExpire()
                }
            })
            start()
        }

        viewModel.isJwtTokenExpired.observe(viewLifecycleOwner) { isExpired ->
            if (isExpired) {
                findNavController().navigate(R.id.action_splashFragment_to_feature_auth_navigation)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_feature_dashboard_navigation)
            }
        }
    }

    private fun playMoveScaling(): AnimatorSet {
        val moveAnimate =
            ObjectAnimator.ofFloat(binding.imgSplashSlide, View.TRANSLATION_Y, 2800f, -100f)
                .setDuration(7000)
        val scalingAnimate =
            ObjectAnimator.ofFloat(binding.imgSplashSlide, View.SCALE_Y, 0f, 5f).setDuration(5000)

        return AnimatorSet().apply {
            playTogether(moveAnimate, scalingAnimate)

        }
    }

    private fun playFadeAnimation(): AnimatorSet {
        val fadeAnimate =
            ObjectAnimator.ofFloat(binding.imgSplashSlide, View.ALPHA, 1f, 0f).setDuration(2000)
        val logoFade = ObjectAnimator.ofFloat(binding.imgLumi, View.ALPHA, 1f, 0f).setDuration(500)

        return AnimatorSet().apply {
            playTogether(fadeAnimate, logoFade)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}