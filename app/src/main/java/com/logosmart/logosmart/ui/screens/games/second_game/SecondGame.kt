package com.azamovhudstc.logosmart.ui.screens.games.second_game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.azamovhudstc.logosmart.databinding.GameScreenTwoBinding
import com.azamovhudstc.logosmart.databinding.ItemPinCustomBinding
import com.azamovhudstc.logosmart.utils.finalResult
import java.util.*


class SecondGame : Fragment() {
    private var _binding: GameScreenTwoBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GameScreenTwoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setUpKeyboard()
        }
        binding.nextBtn.setOnClickListener {
            animateConfirmButton(it)
        }

        binding.restartBtn.setOnClickListener {
            animateConfirmButton(it)
        }


    }

    private fun setUpKeyboard() {
        val rootLayout = binding.rootLayout
        binding.container.removeAllViews()

        val itemPinBindings = mutableListOf<ItemPinCustomBinding>()

        // Har bir child uchun yangi ItemPinCustom qo'shamiz
        for (i in 0 until rootLayout.letterParent.childCount) {
            val itemPinCustomBinding = ItemPinCustomBinding.inflate(
                LayoutInflater.from(binding.container.context),
                binding.container,
                false
            )
            itemPinBindings.add(itemPinCustomBinding)
            binding.container.addView(itemPinCustomBinding.root)
        }
        for (i in 0 until rootLayout.letterParent.childCount) {
            val child = rootLayout.letterParent.getChildAt(i) as TextView
            child.setOnClickListener {
                // Bo'sh ItemPinCustom ni topamiz va uni barchasiga child.text ni qo'shamiz
                val nextAvailableBinding = itemPinBindings.firstOrNull { it.root.text.isEmpty() }
                nextAvailableBinding?.root?.text = child.text
                animateConfirmButton(it)
                finalResult(itemPinBindings,binding,this)
                // Barcha yozuvlarni tekshirish

            }
        }

        // Delete tugmasi uchun click listener qo'shamiz
        binding.rootLayout.deleteKey.setOnClickListener {
            animateConfirmButton(it)
            for (i in itemPinBindings.size - 1 downTo 0) {
                if (itemPinBindings[i].root.text.isNotEmpty()) {
                    itemPinBindings[i].root.text = ""
                    finalResult(itemPinBindings,binding,this)
                    break
                }
            }


        }
    }


    private fun animateConfirmButton(root: View) {
        root.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100)
            .withEndAction {
                root.animate().scaleX(1f).scaleY(1f).setDuration(100)
                    .start()
            }.start()
    }



}