package com.example.belajarmobile

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Pertemuan 10, Latihan 3 — Retrofit dibangun sekali (object + by lazy)
// dan dipakai dari Activity mana pun. Membangun Retrofit itu mahal.
object RetrofitClient {
    // Alamat dasar WAJIB diakhiri "/", kalau tidak Retrofit crash dengan
    // IllegalArgumentException: baseUrl must end in /
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    private const val BASE_URL_KUTIPAN = "https://dummyjson.com/"

    private fun buat(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiService by lazy { buat(BASE_URL).create(ApiService::class.java) }
    val quoteApi: QuoteApiService by lazy { buat(BASE_URL_KUTIPAN).create(QuoteApiService::class.java) }
}
