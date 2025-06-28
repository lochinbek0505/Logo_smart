package com.azamovhudstc.logosmart.ui.screens.games.first_game

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.databinding.FirstGameScreenBinding
import com.azamovhudstc.logosmart.utils.BaseFragment
import com.azamovhudstc.logosmart.utils.animationFromBottomTransaction
import com.azamovhudstc.logosmart.utils.custom.CustomKeyboard


class FirstGameScreen : BaseFragment<FirstGameScreenBinding>(FirstGameScreenBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreate() {


        val customKeyboard = CustomKeyboard(binding.rootLayout, binding.pinView) {
            when (it) {
                1 -> {

                    with(binding.pinView) {
                        setLineColor(Color.parseColor("#18A801"))
                        correct()
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate(
                            R.id.finishGameScreen, null,
                            animationFromBottomTransaction(R.id.firstGameScreen).build()
                        )
                    }, 500)
                }
                -1 -> {
                    with(binding) {
                        pinView.apply {
                            setLineColor(Color.parseColor("#F50000"))
                            shake()
                        }
                    }
                }
                else -> {
                    with(binding) {
                        pinView.apply {
                            setLineColor(Color.parseColor("#00C0E8"))
                        }
                    }
                }
            }
        }
        customKeyboard.setupKeyboard()

    }


}


private fun View.correct() {
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.correct_anim))
}

private fun View.shake() {
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_animation))
}