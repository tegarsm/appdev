package com.tsm.android.appdev.domain

import com.tsm.android.appdev.util.smartTruncate

/**
 * pada bagian ii adalah class data kotlin
 * objek akan akan menampilkan dilayar
 *
 * @see database digunakan untuk objek yang akan dipetakan ke database
 * @see network objek yang digunakan untuk menyiapkan panggilan jaringan
 */

/**
 * Videos devbyte yang dapat diputar.
 */
data class DevByteVideo(val title: String,
                        val description: String,
                        val url: String,
                        val updated: String,
                        val thumbnail: String) {

    /**
     * pada bagian ini digunakan untuk menampilkan deskripsi terpotong di UI
     */
    val shortDescription: String
        get() = description.smartTruncate(200)
}