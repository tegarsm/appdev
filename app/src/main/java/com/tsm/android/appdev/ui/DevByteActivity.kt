package com.tsm.android.appdev.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tsm.android.appdev.R

/**
 * pada bagian ini merupakan aplikasi aktivitas tunggal yang menggunakan pustaka Navigasi.
 */
class DevByteActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev_byte_viewer)
    }
}
