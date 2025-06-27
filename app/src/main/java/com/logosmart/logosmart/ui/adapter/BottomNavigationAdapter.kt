package com.azamovhudstc.logosmart.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.azamovhudstc.logosmart.ui.screens.community.CommunityScreen
import com.azamovhudstc.logosmart.ui.screens.consultation.ConsultationScreen
import com.azamovhudstc.logosmart.ui.screens.games.GamesScreen
import com.azamovhudstc.logosmart.ui.screens.profile.ProfileScreen
import com.azamovhudstc.logosmart.ui.screens.training.TrainingScreen

class BottomNavigationAdapter(fragmentManager: FragmentActivity) :
    FragmentStateAdapter(fragmentManager) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TrainingScreen()
            1 -> GamesScreen()
            2 -> ConsultationScreen()
            3 -> CommunityScreen()
            4 -> ProfileScreen()
            else -> {
                ProfileScreen()
            }
        }
    }
}