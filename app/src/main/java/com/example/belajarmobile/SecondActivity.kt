package com.example.belajarmobile

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// VERSI AKHIR Pertemuan 7: menerima nama, email, DAN hasil olahan dari
// MainActivity (umur + kategori umur), lalu menampilkannya. Inisial nama
// dihitung di sini dari nama yang diterima — contoh logika sederhana
// di layar penerima.
class SecondActivity : AppCompatActivity() {

    companion object {
        // Kunci extra — dipakai bersama oleh pengirim (MainActivity)
        // dan penerima (kelas ini) agar tidak salah ketik.
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_UMUR = "extra_umur"
        const val EXTRA_KATEGORI = "extra_kategori"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val tvInisial = findViewById<TextView>(R.id.tvInisial)
        val tvNamaDiterima = findViewById<TextView>(R.id.tvNamaDiterima)
        val tvEmailDiterima = findViewById<TextView>(R.id.tvEmailDiterima)
        val tvUmurDiterima = findViewById<TextView>(R.id.tvUmurDiterima)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        val nama = intent.getStringExtra(EXTRA_NAMA)
        val email = intent.getStringExtra(EXTRA_EMAIL)
        // Extra bertipe Int dibaca dengan getIntExtra(kunci, nilaiCadangan).
        val umur = intent.getIntExtra(EXTRA_UMUR, -1)
        val kategori = intent.getStringExtra(EXTRA_KATEGORI)

        tvNamaDiterima.text = nama ?: "(nama tidak terkirim)"
        tvEmailDiterima.text = email ?: "(email tidak terkirim)"
        tvUmurDiterima.text = if (umur >= 0) {
            "Umur: $umur tahun • $kategori"
        } else {
            "Umur: (tidak terkirim)"
        }

        // Logika di layar penerima: buat inisial dari nama, mis.
        // "Budi Santoso Pratama" -> "BS" (maksimal 2 huruf pertama kata).
        tvInisial.text = nama
            ?.split(" ")
            ?.filter { it.isNotEmpty() }
            ?.take(2)
            ?.joinToString("") { it.first().uppercase() }
            ?: "?"

        btnKembali.setOnClickListener {
            finish()
        }
    }
}
