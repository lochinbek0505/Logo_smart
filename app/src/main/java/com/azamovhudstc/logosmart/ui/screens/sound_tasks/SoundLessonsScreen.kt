package com.azamovhudstc.logosmart.ui.screens.sound_tasks

import android.os.Bundle
import android.view.animation.LayoutAnimationController
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.findNavController
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.databinding.SoundLessonsScreenBinding
import com.azamovhudstc.logosmart.ui.adapter.SoundLessonAdapter
import com.azamovhudstc.logosmart.utils.BaseFragment
import com.azamovhudstc.logosmart.utils.LocalData
import com.azamovhudstc.logosmart.utils.animationTransaction
import com.azamovhudstc.logosmart.utils.setSlideIn


class SoundLessonsScreen :
    BaseFragment<SoundLessonsScreenBinding>(SoundLessonsScreenBinding::inflate) {
    private val soundLessonAdapter by lazy { SoundLessonAdapter() }
    override fun onViewCreate() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireActivity(), R.color.status)
        binding.lessonRv.layoutAnimation =
            LayoutAnimationController(setSlideIn(), 0.25f)
        soundLessonAdapter.submitList(LocalData.soundLessonList )
        binding.lessonRv.adapter = soundLessonAdapter
        soundLessonAdapter.setTrainItemClickListener { soundLessonModel, position ->
            if (position==0){

                val bundle = Bundle().apply {
                    putString("page", "r")
                }
                findNavController().navigate(R.id.action_soundLessonsScreen_to_gameStartScreen,bundle,
                    animationTransaction().build()
                )

            }
        }

        binding.materialToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}