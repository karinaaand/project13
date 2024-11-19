package com.example.project13.ui

// Import yang diperlukan
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.project13.R
import com.example.project13.database.Buku
import com.example.project13.database.BukuDao
import com.example.project13.database.BukuRoomDatabase
import com.example.project13.databinding.ItemApiBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// Adapter untuk RecyclerView yang digunakan untuk menampilkan daftar buku
class RecyclerAdapter(
    private var items: MutableList<Buku>, // Data daftar buku yang akan ditampilkan
    private val context: Context // Context untuk akses ke sumber daya dan operasi UI
) : RecyclerView.Adapter<RecyclerAdapter.ApiViewHolder>() {

    // ExecutorService untuk menjalankan operasi database di thread terpisah
    private lateinit var executorService: ExecutorService

    // Objek DAO untuk berinteraksi dengan database
    private lateinit var mBukuDao: BukuDao

    // ViewHolder untuk mengikat tampilan dengan data
    inner class ApiViewHolder(val binding: ItemApiBinding) : RecyclerView.ViewHolder(binding.root)

    // Fungsi untuk membuat ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiViewHolder {
        // Inisialisasi ExecutorService
        executorService = Executors.newSingleThreadExecutor()

        // Ambil instance database menggunakan context dari parent
        val db = BukuRoomDatabase.getDatabase(parent.context)

        // Dapatkan Dao dari database
        mBukuDao = db!!.BukuDao()!!

        // Inflasi layout menggunakan ViewBinding
        val binding = ItemApiBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // Mengembalikan objek ViewHolder
        return ApiViewHolder(binding)
    }

    // Mengikat data buku ke tampilan untuk setiap item pada RecyclerView
    override fun onBindViewHolder(holder: ApiViewHolder, position: Int) {
        val apiData = items[position] // Mendapatkan data buku berdasarkan posisi
        holder.binding.judulTxt2.text = apiData.title // Menampilkan judul buku
        updateButton(apiData.bookmark, holder.binding.btnTD2) // Mengatur tombol berdasarkan status bookmark

        // Listener untuk tombol bookmark
        holder.binding.btnTD2.setOnClickListener {
            Toast.makeText(context, "Berhasil update bookmark", Toast.LENGTH_SHORT).show()
            setBookmark(apiData, holder.binding.btnTD2) // Mengubah status bookmark
        }
    }

    // Mengembalikan jumlah item dalam daftar
    override fun getItemCount(): Int {
        return items.size
    }

    // Fungsi untuk mengubah status bookmark pada buku tertentu
    fun setBookmark(buku: Buku, btnTD2: AppCompatButton) {
        executorService.execute {
            mBukuDao.updateBookmarkById(buku.id, !buku.bookmark) // Update bookmark di database
        }
        updateButton(!buku.bookmark, btnTD2) // Perbarui tampilan tombol
    }

    // Fungsi untuk memperbarui data dalam RecyclerView
    fun updateData(newItems: List<Buku>) {
        items.clear() // Hapus data lama
        items.addAll(newItems) // Tambahkan data baru
        notifyDataSetChanged() // Beri tahu adapter bahwa data telah berubah
    }

    // Fungsi untuk memperbarui tampilan tombol bookmark berdasarkan status
    fun updateButton(status: Boolean, btnTD2: AppCompatButton) {
        if (status) {
            // Mengatur background dengan drawable bookmark aktif
            btnTD2.setBackgroundResource(R.drawable.baseline_bookmark_24)
        } else {
            // Mengatur background dengan drawable bookmark tidak aktif
            btnTD2.setBackgroundResource(R.drawable.baseline_bookmark_border_24)
        }
    }
}
