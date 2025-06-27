package com.azamovhudstc.logosmart.ui.screens.main

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.databinding.MainScreenBinding
import com.azamovhudstc.logosmart.ui.adapter.BottomNavigationAdapter
import com.azamovhudstc.logosmart.utils.BaseFragment
import com.azamovhudstc.logosmart.utils.ZoomOutPageTransformer
import com.azamovhudstc.logosmart.utils.showWithAnimation
import com.azamovhudstc.logosmart.utils.visible

class MainScreen : BaseFragment<MainScreenBinding>(MainScreenBinding::inflate) {
    override fun onViewCreate() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)
        setupViewPager()
        setupNavBar()
    }

    private fun setupViewPager() {
        binding.viewPager.apply {
            isUserInputEnabled = false
            setPageTransformer(ZoomOutPageTransformer())
            adapter = BottomNavigationAdapter(requireActivity())
        }
    }

    private fun setupNavBar() {
        binding.navbar.apply {
            visibility = View.VISIBLE
            showWithAnimation(binding.viewPager)
            visible()
            setOnItemSelectedListener { menuItem ->
                handleNavBarItemSelected(menuItem.itemId)
                true
            }
        }
    }

    private fun handleNavBarItemSelected(itemId: Int) {
        val itemIndex = when (itemId) {
            R.id.training -> 0
            R.id.games -> 1
            R.id.consulting -> 2
            R.id.community -> 3
            R.id.profile -> 4
            else -> return
        }

        binding.navbar.apply {
            menu.getItem(itemIndex).isChecked = true
            animate().translationZ(12f).setDuration(200).start()
        }

        binding.viewPager.setCurrentItem(itemIndex, false)
    }
}