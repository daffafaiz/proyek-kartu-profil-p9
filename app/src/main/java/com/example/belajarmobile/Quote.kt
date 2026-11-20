package com.example.belajarmobile

// Pertemuan 10, tugas eksplorasi — API pilihan sendiri: DummyJSON Quotes.
// Berbeda dari JSONPlaceholder, JSON-nya BUKAN array di tingkat atas,
// melainkan objek pembungkus: {"quotes":[...],"total":...}. Karena itu
// hasil panggilannya QuotesResponse, bukan List<Quote>.
data class Quote(
    val id: Int,
    val quote: String,
    val author: String
)

data class QuotesResponse(
    val quotes: List<Quote>
)
