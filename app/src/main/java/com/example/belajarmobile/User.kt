package com.example.belajarmobile

// Pertemuan 10, Latihan 2 — satu objek JSON = satu data class.
// Nama field harus sama persis dengan kunci JSON; Gson mencocokkan
// berdasarkan nama. Kunci lain di JSON (address, company) diabaikan.
data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String
)
