package com.example.project13.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project13.R
import com.example.project13.database.Buku
import com.example.project13.database.BukuDao
import com.example.project13.database.BukuRoomDatabase
import com.example.project13.databinding.ActivityAddBinding
import com.example.project13.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// AddActivity adalah Activity untuk menambahkan data buku ke dalam database.
class AddActivity : AppCompatActivity() {

    // Binding digunakan untuk mengakses elemen pada layout `activity_add.xml`.
    private lateinit var binding: ActivityAddBinding

    // Objek DAO untuk mengakses tabel Buku di database.
    private lateinit var mBukuDao: BukuDao

    // ExecutorService digunakan untuk menjalankan operasi database di thread terpisah.
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        // Menginisialisasi binding untuk menghubungkan layout dengan Activity ini.
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState) // Memanggil onCreate pada superclass.

        // Membuat ExecutorService dengan satu thread.
        executorService = Executors.newSingleThreadExecutor()

        // Mendapatkan instance database menggunakan BukuRoomDatabase.
        val db = BukuRoomDatabase.getDatabase(this)

        // Mendapatkan DAO untuk tabel Buku dari database.
        mBukuDao = db!!.BukuDao()!!

        // Mengatur aksi klik untuk tombol "Add" di layout.
        with(binding) {
            btnAdd.setOnClickListener {
                // Membuat objek Buku dengan data dari input pengguna.
                val buku = Buku(title = edtTitle.text.toString(), bookmark = false)

                // Memasukkan data buku ke dalam database.
                insert(buku)

                // Berpindah kembali ke MainActivity setelah data ditambahkan.
                val intentToTambahdata = Intent(this@AddActivity, MainActivity::class.java)
                startActivity(intentToTambahdata)
            }
        }
    }

    // Fungsi untuk memasukkan data buku ke database menggunakan ExecutorService.
    fun insert(buku: Buku) {
        executorService.execute {
            mBukuDao.insert(buku) // Memasukkan data ke database menggunakan DAO.
        }
    }
}
