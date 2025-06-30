package com.azamovhudstc.logosmart.ui.screens.camera_tasks

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.databinding.ActivityGameBinding
import kotlinx.coroutines.*
import pl.droidsonroids.gif.GifDrawable

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private var gifDrawable: GifDrawable? = null

    private var recordingJob: Job? = null
    private var isGifPlaying = false
    private var hasPlayedOnce = false // faqat bir marta ishlashi uchun
    private val threshold = 8000

    private val RECORD_AUDIO_REQUEST_CODE = 101
    private var ch = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ch = intent.getIntExtra("ch", 0)
        gifDrawable = when (ch) {
            1 -> GifDrawable(resources, R.drawable.parrak)
            2 -> GifDrawable(resources, R.drawable.sham)
            else -> GifDrawable(resources, R.drawable.parrak)
        }

        gifDrawable?.stop() // autoplay bo‘lmasin
        binding.gif.setImageDrawable(gifDrawable)
        binding.playIcon.setOnClickListener {

            if (isGifPlaying){
            when (ch) {
                1 -> {
                    var intent = Intent(this, ModelActivity::class.java)
                    intent.putExtra("ch", 2)
                    startActivity(intent)
                    finish()
                }
                2 -> {
                    var intent = Intent(this, ModelActivity::class.java)
                    intent.putExtra("ch", 3)
                    startActivity(intent)
                    finish()

                }
                else -> playGifFromFrame(10)
            }
                }else{
                Toast.makeText(this, "Iltimos ekranga puflang", Toast.LENGTH_SHORT).show()
            }

        }
        checkAudioPermission()
    }

    private fun checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_REQUEST_CODE
            )
        } else {
            startListening()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_AUDIO_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startListening()
        } else {
            Toast.makeText(this, "Mikrofonga ruxsat kerak", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startListening() {
        recordingJob = CoroutineScope(Dispatchers.IO).launch {
            val bufferSize = AudioRecord.getMinBufferSize(
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )

            val recorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )

            val buffer = ShortArray(bufferSize)
            recorder.startRecording()

            while (isActive) {
                val read = recorder.read(buffer, 0, buffer.size)
                val amplitude = buffer.take(read).maxOrNull()?.toInt() ?: 0

                if (amplitude > threshold && !isGifPlaying && !hasPlayedOnce) {
                    isGifPlaying = true
                    hasPlayedOnce = true
                    withContext(Dispatchers.Main) {
                        if (ch == 2) {
                            playGifFromPosition(10000)
                        } else {
                            playGifFromFrame(10)
                        }
                    }
                }

                delay(100)
            }

            recorder.stop()
            recorder.release()
        }
    }

    private fun playGifFromFrame(frame: Int = 10) {
        gifDrawable?.apply {
            stop()
            seekToFrame(frame)
            loopCount = 1
            addAnimationListener {
                stop()
                seekToFrame(0) // 🔁 GIF boshiga qaytsin va to‘xtasin
            }
            start()
        }
    }

    private fun playGifFromPosition(milliseconds: Int = 10000) {
        gifDrawable?.apply {
            stop()
            seekTo(milliseconds)
            loopCount = 1
            addAnimationListener {
                stop()
                seekTo(0) // 🔁 GIF boshiga qaytsin va to‘xtasin
            }
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        recordingJob?.cancel()
        gifDrawable?.stop()
    }
}
