package com.tsm.android.appdev

import android.app.Application
import timber.log.Timber

/**
 * Ganti aplikasi untuk menyiapkan pekerjaan latar belakang melalui WorkManager
 */
class DevByteApplication : Application() {

    /**
     * onCreate dipanggil sebelum layar pertama ditampilkan kepada pengguna.
     *
     * Gunakan untuk mengatur tugas latar belakang apa pun, menjalankan operasi penyiapan yang mahal di latar belakang
     * utas untuk menghindari keterlambatan memulai aplikasi.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
