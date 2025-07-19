package com.azamovhudstc.logosmart.ui.screens.video

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.data.model.Video
import com.azamovhudstc.logosmart.ui.adapter.VideoAdapter

class VideoActivity2 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video2)
        val back=findViewById<ImageView>(R.id.back)
        recyclerView = findViewById(R.id.videoRecyclerView2)
        back.setOnClickListener {
            finish()
        }
        val videoList = listOf(
            Video("Mansurova Dilnoza", "Uc71i2MjJ0M",
                "Mansurova Dilnoza “Happy speech” logopedik markazi rahbari. 7 yil tajribaga ega logoped. Ish faoliyati davrida autizm, daun va boshqa sindromga ega bolalar bilan ishlab yuqori natijalarga erishgan."
            ),
            Video("Azizova Sabina", "gwo00vo_HwQ","Azizova Sabina “Logopedi Uzbekistana” logopedik markazi rahbari. 8 yillik tajribaga ega logoped. Ish faoliyati davrida og’ir nutq nuqsonlari (alaliya, afaziya, mutizm, va b) nuqsonlar bilan ko’p marotaba ishlab natijaga erishgan."),
            Video("Saydmurodova Gulnoza ", "v8YgSHr2-rY","Saydmurodova Gulnoza “Logogogo”logopedik markazi rahbari. 6 yil tajribaga ega Logoped. Bolalardagi nutq nuqsonlarni bartaraf etish bo’yicha Rossiya amerika davlatlarida o’z malakasini oshirgan."),
            Video("Shukurova Dilbar","K9uofBctA8g","Shukurova Dilbar Logosmart mobil ilovasi asoschisi. 8 yillik tajriba ega logoped defektolog. PhD falsafa doktori hozirgi kunda Logopedik faoliyatga innovatsion texnologiyalarni joriy etish va nutq nuqsonlarni bartaraf etish bo’yicha ilmiy amaliy masalalarni tadqiq etadi.")
        )

        recyclerView.adapter = VideoAdapter(this, videoList)
    }
}
