package com.azamovhudstc.logosmart.ui.screens.start

import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.findNavController
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.databinding.GameStartScreenBinding
import com.azamovhudstc.logosmart.utils.BaseFragment
import com.azamovhudstc.logosmart.utils.animationTransaction

class GameStartScreen: BaseFragment<GameStartScreenBinding>(GameStartScreenBinding::inflate) {

    override fun onViewCreate() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireActivity(), R.color.status)

        var page = arguments?.getString("page").toString()
        if (page == "r") {
            binding.gameStart.setOnClickListener {

                findNavController().navigate(R.id.action_gameStartScreen_to_modelFragment,null,animationTransaction().build())

            }
        } else {
            binding.gameStart.setOnClickListener {
                findNavController().navigate(
                    R.id.dragDropGame, null,
                    animationTransaction().build()
                )
            }
        }
    }
}