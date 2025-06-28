package com.azamovhudstc.logosmart.ui.screens.games.drag_drop

import android.annotation.SuppressLint
import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.azamovhudstc.logosmart.databinding.DragDropGameBinding
import com.azamovhudstc.logosmart.repository.impl.PuzzleRepositoryImpl
import com.azamovhudstc.logosmart.utils.BaseFragment
import com.azamovhudstc.logosmart.utils.custom.alphaAnim
import com.azamovhudstc.logosmart.utils.custom.fadeOut
import com.azamovhudstc.logosmart.utils.custom.snackString
import com.azamovhudstc.logosmart.utils.invisible
import com.azamovhudstc.logosmart.utils.show
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class DragDropGame : BaseFragment<DragDropGameBinding>(DragDropGameBinding::inflate) {


    private val repository = PuzzleRepositoryImpl()

    private val imagesFirst = arrayListOf(
        PointF(0f, 0f),
        PointF(0f, 0f),
        PointF(0f, 0f),
        PointF(0f, 0f),
    )

    private val imagesOld = arrayListOf(
        PointF(0f, 0f),
        PointF(0f, 0f),
        PointF(0f, 0f),
        PointF(0f, 0f),
    )

    private val states = arrayListOf(true, true, true, true)

    private val images = repository.easy

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreate() {

        binding.apply {
            loadImages()
            binding.answer1.setOnTouchListener { v, event ->
                changeEvent(
                    event,
                    imagesFirst[0],
                    imagesOld[0],
                    binding.answer1,
                    binding.question1,
                    0,

                    binding.done1Container
                )
            }
            binding.answer2.setOnTouchListener { v, event ->
                changeEvent(
                    event,
                    imagesFirst[1],
                    imagesOld[1],
                    binding.answer2,
                    binding.question2,
                    1, binding.done2Container
                )
            }
            binding.answer3.setOnTouchListener { v, event ->
                changeEvent(
                    event,
                    imagesFirst[2],
                    imagesOld[2],
                    binding.answer3,
                    binding.question3,
                    2,
                    binding.done3Container
                )
            }
            binding.answer4.setOnTouchListener { v, event ->
                changeEvent(
                    event,
                    imagesFirst[3],
                    imagesOld[3],
                    binding.answer4,
                    binding.question4,
                    3,
                    binding.done4Container
                )
            }
        }
        loadImages()
    }

    private fun loadImages() {
        binding.apply {
            question1.setImageResource(images[0])
            question2.setImageResource(images[1])
            question3.setImageResource(images[2])
            question4.setImageResource(images[3])
            answer1.setImageResource(images[0])
            answer2.setImageResource(images[1])
            answer3.setImageResource(images[2])
            answer4.setImageResource(images[3])
            question1Done.setImageResource(images[0])
            question2Done.setImageResource(images[1])
            question3Done.setImageResource(images[2])
            question4Done.setImageResource(images[3])
        }
    }


    private fun changeEvent(
        event: MotionEvent,
        imageFirstState: PointF,
        imageOldState: PointF,
        imageView: ImageView,
        hintImage: ImageView,
        state: Int,
        doneContainer: FrameLayout
    ): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                imageFirstState.set(event.x, event.y)
                imageOldState.set(imageView.x, imageView.y)
            }
            MotionEvent.ACTION_UP -> {
                val tX = hintImage.x
                val tY = hintImage.y
                val oX = imageView.x
                val oY = imageView.y
                if (Math.abs(tX - oX) < 75f && Math.abs(tY - oY) < 75f) {
                   lifecycleScope.launch {
                       imageView.x = tX
                       imageView.y = tY
                       shakeView(imageView)
                       shakeView(hintImage)
                       delay(500)
                       imageView.fadeOut()
                       hintImage.fadeOut()
                       imageView.invisible()
                       hintImage.invisible()
                       delay(1000)
                       states[state] = false
                       doneContainer.show()
                       doneContainer.alphaAnim()
                       shakeView(doneContainer)
                   }
                    isWin()
                } else {
                    imageView.x = imageOldState.x
                    imageView.y = imageOldState.y
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.x - imageFirstState.x
                val deltaY = event.y - imageFirstState.y
                imageView.x += deltaX
                imageView.y += deltaY
            }
        }
        return true
    }

    private fun isWin() {
        if (!states.any { it }) {
            println("YOU WINNN")
            snackString("YOU WON")
        }
    }


    private fun shakeView(imageView: View) {
        imageView.animate().apply {
            duration = 150
            xBy(6f)
            scaleXBy(0.1f)
            scaleYBy(0.1f)
            withEndAction {
                imageView.animate().apply {
                    duration = 150
                    xBy(-12f)
                    withEndAction {
                        imageView.animate().apply {
                            duration = 50
                            xBy(12f)
                            withEndAction {
                                imageView.animate().apply {
                                    duration = 150
                                    xBy(-6f)
                                    scaleXBy(-0.1f)
                                    scaleYBy(-0.1f)
                                }.start()
                            }.start()
                        }.start()
                    }.start()
                }.start()
            }.start()
        }.start()
    }

}