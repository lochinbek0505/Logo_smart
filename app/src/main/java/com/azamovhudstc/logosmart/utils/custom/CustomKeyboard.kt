package com.azamovhudstc.logosmart.utils.custom

import android.view.View
import com.azamovhudstc.logosmart.databinding.LayoutKeyboardBinding
import com.chaos.view.PinView

class CustomKeyboard(
    private val rootLayout: LayoutKeyboardBinding,
    private val pinView: PinView,
    private val onPinCorrect: (Int) -> Unit
) {

    fun setupKeyboard() {
        val keys = listOf(
            rootLayout.keyQ, rootLayout.keyW, rootLayout.keyE, rootLayout.keyR, rootLayout.keyT,
            rootLayout.keyY, rootLayout.keyU, rootLayout.keyI, rootLayout.keyO, rootLayout.keyP,
            rootLayout.keyA, rootLayout.keyS, rootLayout.keyD, rootLayout.keyF, rootLayout.keyG,
            rootLayout.keyH, rootLayout.keyJ, rootLayout.keyK, rootLayout.keyL,
            rootLayout.keyZ, rootLayout.keyX, rootLayout.keyC, rootLayout.keyV,
            rootLayout.keyB, rootLayout.keyN, rootLayout.keyM
        )
        rootLayout.deleteKey.setOnClickListener {
            animateConfirmButton(it)
            deleteCharacterFromPinView()
        }

        for (keyTextView in keys) {
            keyTextView.setOnClickListener {
                animateConfirmButton(it)
                val keyText = keyTextView.text.toString()
                addCharacterToPinView(keyText)

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

    private fun addCharacterToPinView(character: String) {
        val currentText = pinView.text.toString()
        if (currentText.length < pinView.itemCount) {
            pinView.setText(currentText.plus(character))
        }
        checkPinView()
    }

    private fun deleteCharacterFromPinView() {
        val currentText = pinView.text.toString()
        if (currentText.isNotEmpty()) {
            pinView.setText(currentText.substring(0, currentText.length - 1))
        }
        checkPinView()
    }
    private fun checkPinView() {
        val currentText = pinView.text.toString().lowercase()
        val pinLength = pinView.itemCount

        when {
            currentText.length != pinLength -> onPinCorrect(0)
            currentText == "raketa" -> onPinCorrect(1)
            else -> onPinCorrect(-1)
        }
    }
}