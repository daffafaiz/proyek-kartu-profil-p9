package com.example.belajarmobile

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Pertemuan 10, Latihan 4-6 — mengambil 10 pengguna dari JSONPlaceholder
// dengan enqueue (callback, BUKAN coroutine) dan menampilkannya di
// RecyclerView. Tiga keadaan: memuat, sukses, gagal.
class UserListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val rvUsers = findViewById<RecyclerView>(R.id.rvUsers)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tvStatusUser = findViewById<TextView>(R.id.tvStatusUser)

        rvUsers.layoutManager = LinearLayoutManager(this)

        // Keadaan memuat: tampil sebelum jawaban datang.
        progressBar.visibility = View.VISIBLE
        tvStatusUser.text = "Memuat data pengguna..."

        RetrofitClient.api.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    tvStatusUser.text = ""
                    // Adapter dipasang SETELAH data datang, bukan di onCreate.
                    rvUsers.adapter = UserAdapter(response.body() ?: emptyList())
                } else {
                    // Server menjawab tetapi bukan 2xx (mis. 404): tetap masuk
                    // onResponse, bukan onFailure.
                    tvStatusUser.text = "Server menjawab dengan kode ${response.code()}"
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                // Gagal total: tidak ada internet / server tidak terjangkau.
                progressBar.visibility = View.GONE
                tvStatusUser.text = "Gagal memuat data: ${t.message}"
                Toast.makeText(this@UserListActivity, "Periksa koneksi internet", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
