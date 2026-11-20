package com.example.belajarmobile

import retrofit2.Call
import retrofit2.http.GET

// Pertemuan 10, Latihan 2 — interface yang menjelaskan endpoint.
// Sengaja TANPA suspend: pertemuan ini memakai enqueue (callback);
// coroutine baru di Pertemuan 11.
interface ApiService {
    @GET("users")
    fun getUsers(): Call<List<User>>
}

// API pilihan sendiri (DummyJSON). limit=10 supaya hanya 10 kutipan.
interface QuoteApiService {
    @GET("quotes?limit=10")
    fun getQuotes(): Call<QuotesResponse>
}
