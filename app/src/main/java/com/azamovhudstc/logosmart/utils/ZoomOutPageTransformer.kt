package com.azamovhudstc.logosmart.utils

import android.animation.ObjectAnimator
import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ZoomOutPageTransformer() : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        if (position == 0.0f) {
            setAnimation(
                view.context,
                view,
                300,
                floatArrayOf(1.3f, 1f, 1.3f, 1f),
                0.5f to 0f
            )
            ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0f).setDuration((200 * 1f).toLong()).start()
        }
    }


}
