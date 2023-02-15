package com.tsm.android.appdev.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// objek antar layanan


/**
 * pada bagian ini adalah Layanan retrofit untuk mengambil daftar putar devbyte.
 */
interface DevbyteService {
    @GET("devbytes")
    suspend fun getPlaylist(): NetworkVideoContainer
}

/**
 * pada bagian ini merupakan Titik masuk utama untuk akses jaringan. Panggil seperti `DevByteNetwork.devbytes.getPlaylist()`
 */
object DevByteNetwork {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
            .baseUrl("https://android-kotlin-fun-mars-server.appspot.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    val devbytes = retrofit.create(DevbyteService::class.java)
}
