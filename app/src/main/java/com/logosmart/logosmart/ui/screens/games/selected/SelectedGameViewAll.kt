package com.azamovhudstc.logosmart.ui.screens.games.selected

import android.view.animation.LayoutAnimationController
import androidx.core.content.ContextCompat
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.data.model.GameNestedModel
import com.azamovhudstc.logosmart.databinding.SelectedGameViewAllBinding
import com.azamovhudstc.logosmart.ui.adapter.GameCompatAdapter
import com.azamovhudstc.logosmart.utils.BaseFragment
import com.azamovhudstc.logosmart.utils.setSlideIn
import com.azamovhudstc.logosmart.utils.slideUp

class SelectedGameViewAll: BaseFragment<SelectedGameViewAllBinding>(SelectedGameViewAllBinding::inflate) {

    private val adapter by lazy { GameCompatAdapter() }

    override fun onViewCreate() {
        val data =requireArguments().getSerializable("data") as GameNestedModel
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)

        binding.apply {
            binding.materialToolbar3.slideUp(800,0)
            binding.gameRv.adapter=adapter
            adapter.submitList(data.list)
            binding.gameRv.layoutAnimation= LayoutAnimationController(setSlideIn(), 0.25f)
        }
    }

}