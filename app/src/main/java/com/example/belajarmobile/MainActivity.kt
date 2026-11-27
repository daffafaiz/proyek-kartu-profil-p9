package com.example.belajarmobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

// VERSI AKHIR Pertemuan 9 — Penyimpanan Data Lokal: SharedPreferences
// dan Room Dasar. Lanjutan langsung dari versi akhir Pertemuan 7:
// semua validasi berlapis, perhitungan umur, dan kategori dengan when
// tetap sama. Yang baru: nama, umur, dan kategori terakhir DISIMPAN
// dengan SharedPreferences setelah validasi lolos, lalu DIBACA kembali
// saat aplikasi dibuka — aplikasi kini "mengingat". Kerangka Room
// (Latihan 4) ada di proyek latihan terpisah, latihan-room-p9, sebagai
// bekal Pertemuan 11.
class MainActivity : AppCompatActivity() {

    companion object {
        // Kunci SharedPreferences — dipakai bersama saat menyimpan
        // dan membaca, supaya ejaannya tidak berbeda di dua tempat.
        const val PREFS_PROFIL = "profil"
        const val KEY_NAMA_TERAKHIR = "nama_terakhir"
        const val KEY_UMUR_TERAKHIR = "umur_terakhir"
        const val KEY_KATEGORI_TERAKHIR = "kategori_terakhir"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etNama = findViewById<EditText>(R.id.etNama)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etTahunLahir = findViewById<EditText>(R.id.etTahunLahir)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnBatal = findViewById<Button>(R.id.btnBatal)
        val btnHapusSimpan = findViewById<Button>(R.id.btnHapusSimpan)
        val btnLihatPengguna = findViewById<Button>(R.id.btnLihatPengguna)
        val btnLihatKutipan = findViewById<Button>(R.id.btnLihatKutipan)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        // --- Pertemuan 9, Latihan 2: baca kembali data tersimpan. ---
        // Dibuka dari berkas "profil" yang sama seperti saat menyimpan.
        // getString(kunci, "") memakai "" bila kunci belum pernah
        // disimpan (misalnya pertama kali aplikasi dibuka); ?: ""
        // menangani kemungkinan null, sesuai null safety Pertemuan 3.
        val prefs = getSharedPreferences(PREFS_PROFIL, Context.MODE_PRIVATE)
        val namaTersimpan = prefs.getString(KEY_NAMA_TERAKHIR, "") ?: ""
        val umurTersimpan = prefs.getInt(KEY_UMUR_TERAKHIR, -1)
        val kategoriTersimpan = prefs.getString(KEY_KATEGORI_TERAKHIR, "") ?: ""
        if (namaTersimpan.isNotEmpty()) {
            etNama.setText(namaTersimpan)
            if (umurTersimpan >= 0 && kategoriTersimpan.isNotEmpty()) {
                tvStatus.text = "Terakhir: $namaTersimpan, $umurTersimpan tahun • $kategoriTersimpan"
            }
        }

        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val teksTahun = etTahunLahir.text.toString().trim()

            // --- Latihan 1: validasi berlapis, dari kolom pertama. ---
            // Begitu ada yang tidak lolos, beri tanda error pada kolomnya,
            // pindahkan fokus ke kolom itu, lalu berhenti (return).
            if (nama.isEmpty()) {
                etNama.error = "Nama tidak boleh kosong"
                etNama.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                etEmail.error = "Email tidak boleh kosong"
                etEmail.requestFocus()
                return@setOnClickListener
            }
            // Pemeriksaan format sederhana: harus ada "@" dan "." sesudahnya.
            if (!email.contains("@") || !email.contains(".")) {
                etEmail.error = "Format email belum benar (contoh: nama@mail.com)"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            // --- Latihan 2: konversi aman + validasi angka. ---
            // toIntOrNull() menghasilkan null bila isinya bukan angka,
            // sehingga aplikasi tidak crash seperti bila memakai toInt().
            val tahunLahir = teksTahun.toIntOrNull()
            if (tahunLahir == null) {
                etTahunLahir.error = "Tahun lahir harus berupa angka, mis. 2004"
                etTahunLahir.requestFocus()
                return@setOnClickListener
            }
            val tahunSekarang = Calendar.getInstance().get(Calendar.YEAR)
            if (tahunLahir < 1950 || tahunLahir > tahunSekarang) {
                etTahunLahir.error = "Tahun lahir harus antara 1950 dan $tahunSekarang"
                etTahunLahir.requestFocus()
                return@setOnClickListener
            }

            // --- Latihan 3: logika perhitungan + kategori dengan when. ---
            val umur = tahunSekarang - tahunLahir
            val kategori = when (umur) {
                in 0..12 -> "Anak"
                in 13..17 -> "Remaja"
                in 18..59 -> "Dewasa"
                else -> "Lansia"
            }

            // --- Pertemuan 9, Latihan 1 & 3: simpan SETELAH validasi
            // lolos dan SEBELUM startActivity. apply() menyimpan secara
            // asinkron di latar belakang — pakai apply(), bukan commit().
            prefs.edit()
                .putString(KEY_NAMA_TERAKHIR, nama)
                .putInt(KEY_UMUR_TERAKHIR, umur)
                .putString(KEY_KATEGORI_TERAKHIR, kategori)
                .apply()

            tvStatus.text = "Profil disimpan: $nama"
            Toast.makeText(this, "Halo, $nama!", Toast.LENGTH_SHORT).show()

            // Hasil OLAHAN (umur & kategori) ikut dikirim, bukan hanya
            // data mentah — layar kedua tinggal menampilkannya.
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra(SecondActivity.EXTRA_NAMA, nama)
            intent.putExtra(SecondActivity.EXTRA_EMAIL, email)
            intent.putExtra(SecondActivity.EXTRA_UMUR, umur)
            intent.putExtra(SecondActivity.EXTRA_KATEGORI, kategori)
            startActivity(intent)
        }

        // Klik Batal -> kosongkan formulir, termasuk tanda error.
        btnBatal.setOnClickListener {
            etNama.text.clear()
            etEmail.text.clear()
            etTahunLahir.text.clear()
            etNama.error = null
            etEmail.error = null
            etTahunLahir.error = null
            tvStatus.text = ""
            etNama.requestFocus()
            Toast.makeText(this, "Formulir dibersihkan", Toast.LENGTH_SHORT).show()
        }

        // --- Pertemuan 9, Latihan 3: hapus data tersimpan. ---
        // clear() menghapus semua kunci di berkas "profil" itu.
        btnHapusSimpan.setOnClickListener {
            prefs.edit().clear().apply()
            etNama.text.clear()
            tvStatus.text = "Data tersimpan dihapus."
        }

        // --- Pertemuan 10: buka layar daftar dari internet (REST API). ---
        // Data profil tetap lokal (SharedPreferences); dua layar ini
        // mengambil datanya dari server setiap kali dibuka.
        // Pertemuan 11: tombol dinonaktifkan begitu ditekan, supaya tekan
        // dua kali dengan cepat tidak membuka dua layar. Aktif lagi di onResume.
        btnLihatPengguna.setOnClickListener {
            btnLihatPengguna.isEnabled = false
            startActivity(Intent(this, UserListActivity::class.java))
        }
        btnLihatKutipan.setOnClickListener {
            btnLihatKutipan.isEnabled = false
            startActivity(Intent(this, QuoteListActivity::class.java))
        }

        // Tekan lama tombol Simpan -> tampilkan petunjuk (dari Pertemuan 6).
        btnSimpan.setOnLongClickListener {
            tvStatus.text = "Tekan biasa tombol Simpan untuk menyimpan & pindah layar"
            true
        }
    }

    override fun onResume() {
        super.onResume()
        findViewById<Button>(R.id.btnLihatPengguna).isEnabled = true
        findViewById<Button>(R.id.btnLihatKutipan).isEnabled = true
    }
}
