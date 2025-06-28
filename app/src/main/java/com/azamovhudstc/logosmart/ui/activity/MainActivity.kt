package com.azamovhudstc.logosmart.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.utils.initActivity

class  MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initActivity(this)

    }
}