package com.example.belajarmobile

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

// Pertemuan 11: daftar 10 pengguna, kini dengan coroutine (menggantikan
// enqueue Pertemuan 10). Satu try/catch menangani semua kegagalan dan
// finally memastikan ProgressBar selalu hilang. lifecycleScope membatalkan
// coroutine otomatis saat Activity dihancurkan (Kembali / putar layar).
class UserListActivity : AppCompatActivity() {

    companion object {
        private const val JEDA_SIMULASI_MS = 3000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val rvUsers = findViewById<RecyclerView>(R.id.rvUsers)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tvStatusUser = findViewById<TextView>(R.id.tvStatusUser)
        val btnSimulasi = findViewById<Button>(R.id.btnSimulasi)

        rvUsers.layoutManager = LinearLayoutManager(this)

        fun muat(jedaMs: Long) {
            // Dinonaktifkan selama memuat agar tidak ada dua pemuatan sekaligus.
            btnSimulasi.isEnabled = false
            lifecycleScope.launch {
                try {
                    rvUsers.adapter = null
                    progressBar.visibility = View.VISIBLE
                    tvStatusUser.text = getString(
                        if (jedaMs > 0) R.string.simulasi_berjalan else R.string.memuat_data
                    )

                    // Jaringan (dan simulasi) di IO; utas Main tetap bebas.
                    val users = withContext(Dispatchers.IO) {
                        delay(jedaMs) // menjeda coroutine, BUKAN membekukan utas
                        RetrofitClient.api.getUsers()
                    }

                    // Kembali di Main: aman update UI.
                    rvUsers.adapter = UserAdapter(users)
                    tvStatusUser.text = getString(R.string.pengguna_dimuat)
                } catch (e: CancellationException) {
                    // Layar ditutup saat memuat: batal biasa, bukan kegagalan.
                    throw e
                } catch (e: Exception) {
                    val pesan = if (e is HttpException) getString(R.string.gagal_kode, e.code())
                    else getString(R.string.gagal_terhubung)
                    tvStatusUser.text = pesan
                    Toast.makeText(this@UserListActivity, pesan, Toast.LENGTH_SHORT).show()
                } finally {
                    progressBar.visibility = View.GONE
                    btnSimulasi.isEnabled = true
                }
            }
        }

        btnSimulasi.setOnClickListener { muat(JEDA_SIMULASI_MS) }
        muat(0)
    }
}
