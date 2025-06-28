package com.azamovhudstc.logosmart.ui.screens.start
import androidx.navigation.fragment.findNavController
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.databinding.StartScreenBinding
import com.azamovhudstc.logosmart.utils.BaseFragment
import com.azamovhudstc.logosmart.utils.animationTransaction

class StartScreen: BaseFragment<StartScreenBinding>(StartScreenBinding::inflate) {
    override fun onViewCreate() {


        binding.buttonStart.setOnClickListener {
            findNavController().navigate(
                R.id.gameStartScreen, null,
                animationTransaction().build()
            )
        }
    }
}