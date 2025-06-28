package com.azamovhudstc.logosmart.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.transition.Slide
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.app.LogoApp
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

fun BottomNavigationView.showWithAnimation(fragmentContainerView: View) {
    if (this.visibility == View.VISIBLE) return
    this.visible()
    this.animateTranslationY(0f, 66f, 700)
    fragmentContainerView.animateMarginBottom(66f, 700)
}

fun BottomNavigationView.hideWithAnimation(fragmentContainerView: View) {
    if (this.visibility == View.GONE) return
    this.animateTranslationY(66f, 0f, 700)
    fragmentContainerView.animateMarginBottom(0f, 700)
}
fun toast(string: String?) {
    if (string != null) {
        println(string)
        MainScope().launch {
            Toast.makeText(LogoApp.instance ?: return@launch, string, Toast.LENGTH_SHORT).show()
        }
    }
}


fun BottomNavigationView.hideWithoutAnimation(fragmentContainerView: View) {
    if (this.visibility == View.GONE) return
    this.gone()

    val params =
        fragmentContainerView.layoutParams as ConstraintLayout.LayoutParams
    params.setMargins(
        params.leftMargin,
        params.topMargin,
        params.rightMargin,
        0
    )
    fragmentContainerView.layoutParams = params

}

fun EditText.changeFocusedInputTint(isFocused: Boolean) {

    if (isFocused) {

        for (drawable in this.compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter =
                    PorterDuffColorFilter(
                        ContextCompat.getColor(
                            this.context,
                            R.color.white
                        ),
                        PorterDuff.Mode.SRC_IN
                    )
            }
        }

    } else {
        if (this.text.toString().isEmpty()) {

            for (drawable in this.compoundDrawables) {
                if (drawable != null) {
                    drawable.colorFilter =
                        PorterDuffColorFilter(
                            ContextCompat.getColor(
                                this.context,
                                R.color.grey
                            ),
                            PorterDuff.Mode.SRC_IN
                        )
                }
            }
        }


    }

}


fun animationTransactionClearStack(clearFragmentID: Int): NavOptions.Builder {
    val navBuilder = NavOptions.Builder()
    navBuilder.setEnterAnim(R.anim.from_right).setExitAnim(R.anim.to_left)
        .setPopEnterAnim(R.anim.from_left).setPopExitAnim(R.anim.to_right)
        .setPopUpTo(clearFragmentID, true)
    return navBuilder
}

fun animationTransaction(): NavOptions.Builder {
    val navBuilder = NavOptions.Builder()
    navBuilder.setEnterAnim(R.anim.from_right).setExitAnim(R.anim.to_left)
        .setPopEnterAnim(R.anim.from_left).setPopExitAnim(R.anim.to_right)
    return navBuilder
}

fun animationFromBottomTransaction(clearFragmentID: Int): NavOptions.Builder{
    val slideUp = Slide(Gravity.BOTTOM)
    slideUp.duration = 300

    val slideDown = Slide(Gravity.TOP)
    slideDown.duration = 300

    return NavOptions.Builder()
        .setEnterAnim(R.anim.from_bottom).setPopUpTo(clearFragmentID, true)
}
fun Int.formatTime(): String {
    val hours = this / 60
    val remainingMinutes = this % 60
    return String.format("%01dh %02dm", hours, remainingMinutes)
}


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}


fun View.animateTranslationY(animateFrom: Float, animateTo: Float, duration: Long) {
    val animator =
        ObjectAnimator.ofFloat(
            this, "translationY", TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                animateTo,
                resources.displayMetrics
            ), TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                animateFrom,
                resources.displayMetrics
            )
        )
    animator.duration = duration
    if (animateTo == 0f) {
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@animateTranslationY.gone()
            }
        })
    }
    animator.start()

}

fun View.animateMarginBottom(size: Float, duration: Long) {
    val dpToPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        size,
        resources.displayMetrics
    )


    val params =
        this.layoutParams as ConstraintLayout.LayoutParams
    val animator = ValueAnimator.ofInt(params.bottomMargin, dpToPx.toInt())
    animator.addUpdateListener {
        val value = it.animatedValue as Int
        params.setMargins(
            params.leftMargin,
            params.topMargin,
            params.rightMargin,
            value
        )
        this.layoutParams = params
    }
    animator.duration = duration
    animator.start()
}
