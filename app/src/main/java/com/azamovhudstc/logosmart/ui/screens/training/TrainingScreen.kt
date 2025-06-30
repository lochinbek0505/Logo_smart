package com.azamovhudstc.logosmart.ui.screens.training

import android.content.Intent
import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.databinding.TrainingScreenBinding
import com.azamovhudstc.logosmart.ui.adapter.TrainingAdapter
import com.azamovhudstc.logosmart.utils.BaseFragment
import com.azamovhudstc.logosmart.utils.animationTransaction
import com.azamovhudstc.logosmart.utils.slideStart
import com.azamovhudstc.logosmart.utils.slideUp

class TrainingScreen :
    BaseFragment<TrainingScreenBinding>(com.azamovhudstc.logosmart.databinding.TrainingScreenBinding::inflate) {
    override fun onViewCreate() {

        binding.appbartextcontainer.slideUp(800,0)
        binding.appbarnotifyic.slideStart(800,0)
        binding.movmentContainer.slideUp(800,0)
        binding.lastMovment.slideUp(800,0)
        binding.rightIcon.slideStart(800,0)
        val adapter = TrainingAdapter()
        binding.trainingRv.adapter = adapter
        adapter.setTrainItemClickListener { trainingLayoutsModel, position ->

                when (position) {
                    0 -> {
                        openTelegramBot()
                    }
                    1 -> {
                        findNavController().navigate(
                            com.azamovhudstc.logosmart.R.id.startScreen,
                            null,
                            animationTransaction().build()
                        )
                    }
                    2 -> {
                        findNavController().navigate(
                            R.id.soundLessonsScreen, null,
                            animationTransaction().build()
                        )
                    }
                }
            }

    }
    private fun openTelegramBot() {
        val telegramBotUsername = "logosmartbot"
        val url = "https://t.me/$telegramBotUsername"

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            setPackage("org.telegram.messenger")
        }

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }

}