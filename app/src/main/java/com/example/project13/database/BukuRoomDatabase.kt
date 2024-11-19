package com.example.project13.database

// Import yang diperlukan untuk konfigurasi Room Database
import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

// Deklarasi Room Database dengan entitas Buku
@Database(entities = [Buku::class], version = 1, exportSchema = false)
abstract class BukuRoomDatabase : RoomDatabase() {

    // Menyediakan akses ke DAO (BukuDao)
    abstract fun BukuDao(): BukuDao?

    companion object {
        // Menyimpan instance singleton dari database
        @Volatile
        private var INSTANCE: BukuRoomDatabase? = null

        // Fungsi untuk mendapatkan instance database
        fun getDatabase(context: Context): BukuRoomDatabase? {
            if (INSTANCE == null) { // Jika INSTANCE belum ada, buat database baru
                synchronized(BukuRoomDatabase::class.java) { // Menjaga thread-safety
                    INSTANCE = databaseBuilder(
                        context.applicationContext, // Menggunakan context aplikasi
                        BukuRoomDatabase::class.java, // Kelas database
                        "buku_database" // Nama file database
                    ).build()
                }
            }
            return INSTANCE // Mengembalikan instance database
        }
    }
}
