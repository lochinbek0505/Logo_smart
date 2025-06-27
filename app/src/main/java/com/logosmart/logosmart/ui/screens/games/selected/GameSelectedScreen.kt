package com.azamovhudstc.logosmart.ui.screens.games.selected

import android.os.Bundle
import android.view.animation.LayoutAnimationController
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.databinding.GameAgeScreenBinding
import com.azamovhudstc.logosmart.ui.adapter.GameParentAdapter
import com.azamovhudstc.logosmart.utils.*

class GameSelectedScreen : BaseFragment<GameAgeScreenBinding>(GameAgeScreenBinding::inflate) {
    private val adapter by lazy { GameParentAdapter() }
    override fun onViewCreate() {
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireActivity(), R.color.status)
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.status)

        binding.materialToolbar2.slideUp(800, 0)
        binding.gameRv.adapter = adapter
        binding.gameRv.layoutAnimation = LayoutAnimationController(setSlideIn(), 0.25f)
        adapter.submitList(LocalData.gameList)
        adapter.setViewAllClickListener {
            val bundle = Bundle()
            bundle.putSerializable("data", it)
            findNavController().navigate(
                R.id.selectedGameViewAll,
                bundle,
                animationTransaction().build()
            )
        }
    }
}