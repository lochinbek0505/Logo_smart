package com.azamovhudstc.logosmart.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.databinding.GameScreenTwoBinding
import com.azamovhudstc.logosmart.databinding.ItemPinCustomBinding
import java.util.*

fun checkAllItemsEqual(itemPinBindings: MutableList<ItemPinCustomBinding>): Boolean {
    val texts = itemPinBindings.map { it.root.text.toString().uppercase(Locale.ROOT) }
    println(texts.toList().joinToString(""))
    return texts.toList().joinToString("").equals("RAKETA")
}
fun finalResult(itemPinBindings:MutableList<ItemPinCustomBinding>,binding: GameScreenTwoBinding,fragment:Fragment){
    when {
        itemPinBindings.all { it.root.text.isNotEmpty() } -> when(checkAllItemsEqual(itemPinBindings)) {
            true -> {
                itemPinBindings.onEach { it ->
                    val colorValue = Color.parseColor("#18A801")
                    it.root.strokeColor = ColorStateList.valueOf(colorValue)
                    binding.container.apply {
                        correct()
                    }
                }
                fragment.findNavController().navigate(R.id.finishGameScreen,null,
                    animationTransaction().build()
                )

            }
            false -> {
                binding.container.apply { shake() }
                itemPinBindings.onEach {
                    val colorValue = Color.parseColor("#F50000")
                    it.root.strokeColor = ColorStateList.valueOf(colorValue)
                }
            }
        }
        else -> itemPinBindings.onEach {
            it.root.strokeColor = ColorStateList.valueOf(Color.parseColor("#00C0E8"))
        }
    }
}

private fun View.correct() {
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.correct_anim))
}

private fun View.shake() {
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_animation))
}