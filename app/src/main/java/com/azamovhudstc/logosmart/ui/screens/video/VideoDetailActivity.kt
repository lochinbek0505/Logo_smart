package com.azamovhudstc.logosmart.ui.screens.video

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.azamovhudstc.logosmart.R
import com.squareup.picasso.Picasso

class VideoDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_VIDEO_ID = "extra_video_id"
        const val EXTRA_DESCRIPTION = "extra_description"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)

        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Noma'lum"
        val videoId = intent.getStringExtra(EXTRA_VIDEO_ID) ?: ""
        val description = intent.getStringExtra(EXTRA_DESCRIPTION) ?: ""

        val thumbnail = findViewById<ImageView>(R.id.thumbnail)
        val titleText = findViewById<TextView>(R.id.appBar)
        val descriptionText = findViewById<TextView>(R.id.description)
        var back=findViewById<ImageView>(R.id.back)

        back.setOnClickListener {
            finish()
        }
        titleText.text = title
        descriptionText.text = description

        val thumbnailUrl = "https://img.youtube.com/vi/$videoId/0.jpg"
        Picasso.get().load(thumbnailUrl).into(thumbnail)


    }
}
