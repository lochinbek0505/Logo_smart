package com.azamovhudstc.logosmart.ui.screens.splash

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.databinding.SplashScreenBinding
import com.azamovhudstc.logosmart.utils.BaseFragment
import com.azamovhudstc.logosmart.utils.animationTransactionClearStack
import com.azamovhudstc.logosmart.utils.custom.alphaAnim
import com.azamovhudstc.logosmart.utils.show
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : BaseFragment<SplashScreenBinding>(SplashScreenBinding::inflate) {
    override fun onViewCreate() {

        lifecycleScope.launch {
            binding.logo.show()
            binding.logo.alphaAnim()
            delay(1500)
            findNavController().navigate(
                R.id.mainScreen,
                null,
                animationTransactionClearStack(R.id.splashScreen).build()
            )
        }
    }

}