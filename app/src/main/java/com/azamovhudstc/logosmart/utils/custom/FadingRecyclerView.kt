package com.azamovhudstc.logosmart.utils.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.app.LogoApp

class FadingEdgeRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun isPaddingOffsetRequired(): Boolean {
        return !clipToPadding
    }

    override fun getLeftPaddingOffset(): Int {
        return if (clipToPadding) 0 else -paddingLeft
    }

    override fun getTopPaddingOffset(): Int {
        return if (clipToPadding) 0 else -paddingTop
    }

    override fun getRightPaddingOffset(): Int {
        return if (clipToPadding) 0 else paddingRight
    }

    override fun getBottomPaddingOffset(): Int {
        return if (clipToPadding) 0 else paddingBottom
    }


}
fun View.alphaAnim() {
    val anim = AnimationUtils.loadAnimation(
        currContext()!!,
        R.anim.alpha_anim
    ).apply {
        duration = 1000L

        fillAfter = true
    }

    startAnimation(anim)

}

fun currContext(): Context? {
    return LogoApp.currentContext()
}
fun View.fadeOut() {
    val anim = AnimationUtils.loadAnimation(
        currContext()!!,
        R.anim.fade_out
    ).apply {
        duration = 800L

        fillAfter = true
    }

    startAnimation(anim)

}
