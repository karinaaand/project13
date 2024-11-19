package com.example.project13.ui

// Import yang diperlukan
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import com.example.project13.database.BukuDao
import com.example.project13.database.BukuRoomDatabase
import com.example.project13.databinding.ActivityBookmarkBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// Aktivitas untuk menampilkan daftar buku yang memiliki bookmark
class BookmarkActivity : AppCompatActivity() {

    // View binding untuk mengakses komponen tampilan
    private lateinit var binding: ActivityBookmarkBinding

    // Objek DAO untuk mengakses data dari database
    private lateinit var mBukuDao: BukuDao

    // ExecutorService untuk menjalankan operasi database di thread terpisah
    private lateinit var executorService: ExecutorService

    // Adapter untuk menampilkan daftar judul buku dalam ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengaktifkan mode Edge-to-Edge pada tampilan
        enableEdgeToEdge()

        // Inisialisasi ExecutorService dan DAO
        executorService = Executors.newSingleThreadExecutor()
        val db = BukuRoomDatabase.getDatabase(this)
        mBukuDao = db!!.BukuDao()!!

        // Inisialisasi view binding
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup ListView dan ArrayAdapter
        adapter = ArrayAdapter(
            this@BookmarkActivity, // Context aktivitas ini
            android.R.layout.simple_list_item_1, // Layout bawaan untuk item ListView
            mutableListOf() // Data awal sebagai list kosong
        )
        binding.listView.adapter = adapter // Menghubungkan adapter dengan ListView

        // Memuat data buku yang memiliki bookmark
        getAllNotes()
    }

    // Fungsi untuk mendapatkan data semua buku yang memiliki bookmark
    fun getAllNotes() {
        // Mengamati LiveData dari DAO untuk data buku yang memiliki bookmark
        mBukuDao.BookmarkBuku.observe(this) { notes ->
            // Mengubah daftar Buku menjadi daftar String (judul buku saja)
            val titles = notes.map { it.title }

            // Membersihkan data lama di adapter dan menambahkan data baru
            adapter.clear()
            adapter.addAll(titles)

            // Memberitahu adapter bahwa data telah berubah sehingga UI diperbarui
            adapter.notifyDataSetChanged()
        }
    }
}
