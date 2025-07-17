package com.azamovhudstc.logosmart.utils

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.viewbinding.ViewBinding
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.app.LogoApp
import kotlin.jvm.internal.Intrinsics
import kotlin.reflect.KClass


fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)

fun <T : ViewBinding> ViewGroup.inflateViewBinding(
    bindingClass: KClass<T>,
    attachToRoot: Boolean = false
): T {
    val method = bindingClass.java.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    return method.invoke(null, LayoutInflater.from(this.context), this, attachToRoot) as T
}

private fun View.addRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
    setBackgroundResource(resourceId)
}


fun View.slideUp(animTime: Long, startOffset: Long) {
    val slideUp = AnimationUtils.loadAnimation(LogoApp.instance, R.anim.slide_up).apply {
        duration = animTime
        interpolator = FastOutSlowInInterpolator()
        this.startOffset = startOffset
    }
    startAnimation(slideUp)
}

fun View.slideStart(animTime: Long, startOffset: Long, hide: View? = null) {
    val slideUp = AnimationUtils.loadAnimation(LogoApp.instance, R.anim.slide_start).apply {
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                hide?.hide()

            }

            override fun onAnimationEnd(animation: Animation?) {
                hide?.hide()
            }

            override fun onAnimationRepeat(animation: Animation?) {
                TODO("Not yet implemented")
            }

        })
        duration = animTime
        interpolator = FastOutSlowInInterpolator()
        this.startOffset = startOffset
    }
    startAnimation(slideUp)
}
fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}
fun View.invisible() {
    visibility = View.INVISIBLE
}

object ViewGroupExtensionsKt {
    fun inflate(`$this$inflate`: ViewGroup, resId: Int): View {
        val inflate =
            LayoutInflater.from(`$this$inflate`.context).inflate(resId, `$this$inflate`, false)
        Intrinsics.checkNotNullExpressionValue(
            inflate,
            "from(this.context).inflate(resId, this, false)"
        )
        return inflate
    }


}
