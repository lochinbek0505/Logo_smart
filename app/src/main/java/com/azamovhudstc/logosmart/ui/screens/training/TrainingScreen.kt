package com.azamovhudstc.logosmart.ui.screens.training

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
                        findNavController().navigate(
                            com.azamovhudstc.logosmart.R.id.startScreen,
                            null,
                            animationTransaction().build()
                        )
                    }
                    1 -> {
                        findNavController().navigate(
                            R.id.soundLessonsScreen, null,
                            animationTransaction().build()
                        )
                    }
                }
            }

    }
}