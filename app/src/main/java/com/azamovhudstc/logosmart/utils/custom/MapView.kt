package com.azamovhudstc.logosmart.utils.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.azamovhudstc.logosmart.R

class MapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint()
    private val levels = mutableListOf<Level>()
    private var levelBitmap: Bitmap? = null
    private var starBitmap: Bitmap? = null

    init {
        levelBitmap = BitmapFactory.decodeResource(resources, R.drawable.level_icon)
        starBitmap = BitmapFactory.decodeResource(resources, R.drawable.star_icon)

        // Initialize your levels here
        levels.add(Level(1, 100f, 200f))
        levels.add(Level(2, 200f, 300f))
        levels.add(Level(3, 300f, 400f))
        levels.add(Level(4, 500f,650f))
        levels.add(Level(5, 500f, 600f))
        levels.add(Level(6, 600f, 700f))
        levels.add(Level(7, 700f, 800f))
        levels.add(Level(8, 800f, 900f))
        levels.add(Level(9, 900f, 300f))
        levels.add(Level(10, 200f, 300f))
        levels.add(Level(11, 200f, 300f))
        levels.add(Level(12, 200f, 300f))
        levels.add(Level(13, 200f, 300f))
        // Add more levels as needed
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the background
        canvas.drawColor(Color.GREEN) // Replace with actual background

        // Draw levels
        for (level in levels) {
            // Draw level icon
            levelBitmap?.let {
                canvas.drawBitmap(it, level.x - (it.width / 2), level.y - (it.height / 2), paint)
            }

            // Draw stars
            for (i in 0 until level.stars) {
                starBitmap?.let {
                    canvas.drawBitmap(it, level.x + (i * 30) - 30, level.y - 60, paint)
                }
            }
        }
    }

    data class Level(val number: Int, val x: Float, val y: Float, val stars: Int = 3)
}
