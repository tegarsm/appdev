package com.tsm.android.appdev.network

import com.tsm.android.appdev.domain.DevByteVideo
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects digunakan untuk memparsing respons dari server
 *
 * @see domain sebagai package
 */

/**
 * VideoHolder diguanakn untuk memegang daftar vidio
 *
 * dan digunakan untuk memparsing leel pertama dari hasil jaringan yang terlihat
 *
 * {
 *   "videos": []
 * }
 */
@JsonClass(generateAdapter = true)
data class NetworkVideoContainer(val videos: List<NetworkVideo>)

/**
 * Videos mewakili devbyte yang dapat diputar.
 */
@JsonClass(generateAdapter = true)
data class NetworkVideo(
        val title: String,
        val description: String,
        val url: String,
        val updated: String,
        val thumbnail: String,
        val closedCaptions: String?)

/**
 * pada bagian ini digunakan untuk mengKonversikan hasil Jaringan ke objek basis data
 */
fun NetworkVideoContainer.asDomainModel(): List<DevByteVideo> {
    return videos.map {
        DevByteVideo(
                title = it.title,
                description = it.description,
                url = it.url,
                updated = it.updated,
                thumbnail = it.thumbnail)
    }
}
