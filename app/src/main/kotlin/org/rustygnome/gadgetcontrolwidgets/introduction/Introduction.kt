package org.rustygnome.gadgetcontrolwidgets.introduction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.databinding.IntroductionBinding

class Introduction: Fragment() {

    private var _binding: IntroductionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = IntroductionBinding.inflate(inflater, container, false)
        initToggleUsageVisibility()
        initUsageSteps()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUsageSteps() {
        resources.getStringArray(R.array.introduction_usage_steps).also {
            binding.introductionUsageSteps.text = buildString {
                it.forEachIndexed { index, string ->
                    append("${index+1}. $string\n")
                }
            }
        }
    }

    private fun initToggleUsageVisibility() =
        binding.introductionUsageToggle
            .setOnClickListener {
                binding.introductionUsageSteps.also {
                    when(it.visibility) {
                        View.VISIBLE -> toggleUsageStepsToGone(it)
                        else -> toggleUsageStepsToVisible(it)
                    }
                }
            }

    private fun toggleUsageStepsToVisible(view: View) {
        view.visibility = View.VISIBLE
        AnimationUtils.loadAnimation(requireContext(), R.anim.expand_vertical).also {
            it.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    view.visibility = View.VISIBLE
                }
                override fun onAnimationEnd(animation: Animation?) {
                    binding.introductionUsageToggle.setIconResource(R.drawable.ic_expand_less)
                }
                override fun onAnimationRepeat(animation: Animation?) {}
            })
            view.startAnimation(it)
        }
    }

    private fun toggleUsageStepsToGone(view: View) {
        AnimationUtils.loadAnimation(requireContext(), R.anim.collapse_vertical).also {
            it.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    binding.introductionUsageToggle.setIconResource(R.drawable.ic_expand_more)
                }
                override fun onAnimationEnd(animation: Animation?) {
                    view.visibility = View.GONE
                }
                override fun onAnimationRepeat(animation: Animation?) {}
            })
            view.startAnimation(it)
        }
    }
}
