package com.azamovhudstc.logosmart.ui.screens.camera_tasks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.databinding.ActivityVideoBinding
import com.azamovhudstc.logosmart.ui.activity.MainActivity

class VideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding
    private var isPlaying = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Video manzili (res/raw/example.mp4)
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.video}")
        binding.videoView.setVideoURI(videoUri)

        // MediaController o‘chirib qo‘yiladi
        binding.videoView.setMediaController(null)
        binding.videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            binding.videoView.start()
        }

        // Pauza tugmasi
        binding.playPauseBtn.setOnClickListener {
            if (binding.videoView.isPlaying) {
                binding.videoView.pause()
                binding.playPauseBtn.setImageResource(R.drawable.ic_play)
                isPlaying = false
            } else {
                binding.videoView.start()
                binding.playPauseBtn.setImageResource(R.drawable.pause)
                isPlaying = true
            }
        }

        // Orqaga tugmasi
        binding.playPauseBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}