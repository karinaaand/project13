package com.example.project13.ui

// Import yang diperlukan
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project13.database.BukuDao
import com.example.project13.database.BukuRoomDatabase
import com.example.project13.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// Aktivitas utama untuk menampilkan daftar buku
class MainActivity : AppCompatActivity() {

    // View binding untuk mengakses elemen UI di layout XML
    private lateinit var binding: ActivityMainBinding

    // Objek DAO untuk berinteraksi dengan database
    private lateinit var mBukuDao: BukuDao

    // ExecutorService untuk menjalankan operasi database di thread terpisah
    private lateinit var executorService: ExecutorService

    // Adapter untuk RecyclerView
    private lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengaktifkan mode Edge-to-Edge untuk tampilan
        enableEdgeToEdge()

        // Inisialisasi ExecutorService dan DAO
        executorService = Executors.newSingleThreadExecutor()
        val db = BukuRoomDatabase.getDatabase(this)
        mBukuDao = db!!.BukuDao()!!

        // Inisialisasi view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Konfigurasi RecyclerView
        binding.rvRc.layoutManager = LinearLayoutManager(this) // Menggunakan LinearLayoutManager
        adapter = RecyclerAdapter(mutableListOf(), this@MainActivity) // Inisialisasi adapter dengan data kosong
        binding.rvRc.adapter = adapter // Menghubungkan RecyclerView dengan adapter

        // Memuat semua data buku dari database
        getAllNotes()

        // Listener untuk tombol navigasi
        with(binding) {
            // Navigasi ke BookmarkActivity
            btnBM.setOnClickListener {
                val intentToBookmark = Intent(this@MainActivity, BookmarkActivity::class.java)
                startActivity(intentToBookmark)
            }

            // Navigasi ke AddActivity untuk menambah buku
            btnTD.setOnClickListener {
                val intentToTambahdata = Intent(this@MainActivity, AddActivity::class.java)
                startActivity(intentToTambahdata)
            }
        }
    }

    // Fungsi untuk mendapatkan semua data buku dari database
    fun getAllNotes() {
        // Mengamati LiveData dari DAO untuk semua data buku
        mBukuDao.allBuku.observe(this) { notes ->
            // Perbarui data di adapter dengan data buku terbaru
            adapter.updateData(notes)

            // Tampilkan jumlah total buku sebagai Toast
            Toast.makeText(this@MainActivity, "Total Notes: ${notes.size}", Toast.LENGTH_SHORT).show()
        }
    }
}
