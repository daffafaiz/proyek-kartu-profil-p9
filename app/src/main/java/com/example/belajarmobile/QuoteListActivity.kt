package com.example.belajarmobile

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

// API pilihan sendiri (DummyJSON Quotes), pola coroutine yang sama dengan
// UserListActivity (Pertemuan 11).
class QuoteListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quote_list)

        val rvQuotes = findViewById<RecyclerView>(R.id.rvQuotes)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tvStatusQuote = findViewById<TextView>(R.id.tvStatusQuote)

        rvQuotes.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                tvStatusQuote.text = getString(R.string.memuat_data)

                val hasil = withContext(Dispatchers.IO) {
                    RetrofitClient.quoteApi.getQuotes()
                }

                rvQuotes.adapter = QuoteAdapter(hasil.quotes)
                tvStatusQuote.text = getString(R.string.kutipan_dimuat)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val pesan = if (e is HttpException) getString(R.string.gagal_kode, e.code())
                else getString(R.string.gagal_terhubung)
                tvStatusQuote.text = pesan
                Toast.makeText(this@QuoteListActivity, pesan, Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
