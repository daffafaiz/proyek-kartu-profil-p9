package com.example.belajarmobile

import retrofit2.http.GET

// Interface yang menjelaskan endpoint.
// Pertemuan 11: suspend fun, tanpa Call dan tanpa enqueue. Fungsi bisa
// dijeda saat menunggu server tanpa memblokir utas UI.
interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}

// API pilihan sendiri (DummyJSON). limit=10 supaya hanya 10 kutipan.
interface QuoteApiService {
    @GET("quotes?limit=10")
    suspend fun getQuotes(): QuotesResponse
}
