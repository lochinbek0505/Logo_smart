package com.azamovhudstc.logosmart.ui.screens.finish

import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.databinding.LevelScreenBinding
import com.azamovhudstc.logosmart.utils.BaseFragment
import kotlin.random.Random

class FinishGameScreen: BaseFragment<LevelScreenBinding>(LevelScreenBinding::inflate) {
    override fun onViewCreate() {
        val starScore = Random(20).nextInt(1,4)
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireActivity(), R.color.status)
        binding.starImg.setImageResource(when(starScore) {
            1 -> R.drawable.ic_first_start
            2-> R.drawable.ic_complex_star
            3 -> R.drawable.ic_star_complete
            else -> R.drawable.ic_no_star
        })

    }
}