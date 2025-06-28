package com.azamovhudstc.logosmart.ui.screens.games

import android.view.animation.LayoutAnimationController
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.databinding.GamesScreenBinding
import com.azamovhudstc.logosmart.ui.adapter.GameAdapter
import com.azamovhudstc.logosmart.utils.*


class GamesScreen : BaseFragment<GamesScreenBinding>(GamesScreenBinding::inflate) {
    private val adapter by lazy { GameAdapter() }
    override fun onViewCreate() {
        binding.appbartextcontainer.slideUp(800,0)
        binding.appbarnotifyic.slideStart(800,0)
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)

        binding.gameRv.layoutAnimation = LayoutAnimationController(setSlideIn(), 0.25f)
        binding.gameRv.adapter = adapter
        adapter.submitList(LocalData.gameTypeList)
        adapter.setItemClickedListener {
            findNavController().navigate(R.id.gameSelectedScreen,null, animationTransaction().build())
        }
    }

}