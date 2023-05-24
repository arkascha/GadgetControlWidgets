package org.rustygnome.gadgetcontrolwidgets.introduction

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.databinding.IntroductionBinding
import org.rustygnome.gadgetcontrolwidgets.extension.string.firstCharToUpperCase
import org.rustygnome.gadgetcontrolwidgets.extension.string.addItemsAsBulletList
import timber.log.Timber

class IntroductionActivity: AppCompatActivity() {

    private lateinit var binding: IntroductionBinding

    init {
        Timber.d("> init()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("> onCreate()");

        binding = IntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        Timber.v("> onResume()")

        initToggleUsageVisibility()
        initUsageSteps()
    }

    private fun initUsageSteps() {
        binding.introductionUsageSteps.addItemsAsBulletList(
            resources.getStringArray(R.array.introduction_usage_steps)
                .map { it.firstCharToUpperCase() }
        )
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
        AnimationUtils.loadAnimation(this, R.anim.expand_vertical).also {
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
        AnimationUtils.loadAnimation(this, R.anim.collapse_vertical).also {
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
